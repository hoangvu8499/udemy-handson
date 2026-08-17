package com.fullstack.service;

import com.fullstack.dto.LoginRequest;
import com.fullstack.dto.RegisterRequest;
import com.fullstack.dto.RegisterResponse;
import com.fullstack.dto.TokenResponse;
import com.fullstack.dto.VerifyOtpRequest;
import com.fullstack.entity.AuthSessionAction;
import com.fullstack.entity.AuthSessionLog;
import com.fullstack.entity.OtpChannel;
import com.fullstack.entity.OtpPurpose;
import com.fullstack.entity.User;
import com.fullstack.entity.UserStatus;
import com.fullstack.jwt.JwtTokenProvider;
import com.fullstack.repository.AuthSessionLogRepository;
import com.fullstack.repository.UserRepository;
import com.fullstack.util.PhoneNumberUtils;
import com.fullstack.exception.BusinessException;
import com.fullstack.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthSessionLogRepository authSessionLogRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final RateLimitService rateLimitService;
    private final TokenService tokenService;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${auth.support-hotline}")
    private String supportHotline;

    /**
     * Dang ky bang so dien thoai. User UNVERIFIED dang ky lai (vd: OTP het han)
     * thi chi gui OTP moi tren ban ghi cu, khong tao ban ghi moi.
     */
    @Transactional
    public RegisterResponse register(RegisterRequest request, String requestIp, String userAgent) {
        String phoneNumber = PhoneNumberUtils.normalize(request.getPhoneNumber());

        User user = userRepository.findByPhone(phoneNumber).orElse(null);
        if (user != null) {
            if (user.getStatus() == UserStatus.BLACK_LIST) {
                throw new BusinessException(ErrorCode.USER_BLACKLISTED, blacklistMessage());
            }
            if (user.getStatus() != UserStatus.UNVERIFIED) {
                throw new BusinessException(ErrorCode.USER_ALREADY_EXISTS,
                        "So dien thoai da duoc dang ky. Vui long dang nhap hoac dung chuc nang quen mat khau.");
            }
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        } else {
            user = new User();
            user.setPhone(phoneNumber);
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
            user.setStatus(UserStatus.UNVERIFIED);
        }
        user = userRepository.save(user);

        long otpTtl = otpService.generateAndSend(user.getId(), phoneNumber, OtpChannel.SMS,
                OtpPurpose.REGISTER, requestIp, userAgent);

        return RegisterResponse.builder()
                .userId(user.getId())
                .phoneNumber(phoneNumber)
                .status(user.getStatus().name())
                .otpExpiresInSeconds(otpTtl)
                .build();
    }

    /**
     * noRollbackFor: cac update bo dem sai OTP / trang thai BLACK_LIST phai duoc
     * commit ngay ca khi tra loi nghiep vu cho khach hang.
     */
    @Transactional(noRollbackFor = BusinessException.class)
    public void verifyOtp(VerifyOtpRequest request) {
        String phoneNumber = PhoneNumberUtils.normalize(request.getPhoneNumber());

        User user = userRepository.findByPhone(phoneNumber)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        if (user.getStatus() == UserStatus.BLACK_LIST) {
            throw new BusinessException(ErrorCode.USER_BLACKLISTED, blacklistMessage());
        }
        if (user.getStatus() == UserStatus.ACTIVE) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "Tai khoan da duoc xac thuc, vui long dang nhap.");
        }

        OtpVerifyResult result = otpService.verify(phoneNumber, OtpPurpose.REGISTER, request.getOtpCode());
        switch (result.getOutcome()) {
            case SUCCESS:
                user.setStatus(UserStatus.ACTIVE);
                userRepository.save(user);
                log.info("User {} verified and activated", user.getId());
                return;
            case WRONG:
                throw new BusinessException(ErrorCode.INVALID_OTP, String.format(
                        "Ma OTP khong chinh xac. Ban da nhap sai %d/%d lan, con lai %d lan thu.",
                        result.getAttemptCount(), result.getMaxAttempts(), result.getRemainingAttempts()));
            case EXHAUSTED:
                user.setStatus(UserStatus.BLACK_LIST);
                userRepository.save(user);
                log.warn("User {} blacklisted after {} failed OTP attempts", user.getId(), result.getAttemptCount());
                throw new BusinessException(ErrorCode.OTP_ATTEMPTS_EXCEEDED, String.format(
                        "Ban da nhap sai ma OTP %d/%d lan. Tai khoan da bi vo hieu hoa va khong the dang ky lai. %s",
                        result.getAttemptCount(), result.getMaxAttempts(), blacklistMessage()));
            case EXPIRED:
            case NOT_FOUND:
            default:
                throw new BusinessException(ErrorCode.INVALID_OTP,
                        "Ma OTP da het han hoac khong ton tai. Vui long goi lai API dang ky de nhan ma OTP moi.");
        }
    }

    @Transactional
    public TokenResponse login(LoginRequest request, String requestIp, String userAgent) {
        String phoneNumber = PhoneNumberUtils.normalize(request.getPhoneNumber());

        rateLimitService.checkLoginLimit(phoneNumber);

        User user = userRepository.findByPhone(phoneNumber)
                .orElseThrow(() -> new BusinessException(ErrorCode.BAD_CREDENTIALS));
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BusinessException(ErrorCode.BAD_CREDENTIALS);
        }
        if (user.getStatus() == UserStatus.BLACK_LIST) {
            throw new BusinessException(ErrorCode.USER_BLACKLISTED, blacklistMessage());
        }
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new BusinessException(ErrorCode.USER_NOT_ACTIVE);
        }

        String jti = UUID.randomUUID().toString();
        String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), phoneNumber, jti);
        tokenService.storeAccessToken(jti, user.getId());
        String refreshToken = tokenService.createRefreshToken(user.getId(), jti);

        saveSessionLog(user.getId(), phoneNumber, AuthSessionAction.LOGIN, jti, requestIp, userAgent);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresInSeconds(jwtTokenProvider.getAccessTokenExpirationMs() / 1000)
                .build();
    }

    @Transactional
    public TokenResponse login2(LoginRequest request, String requestIp, String userAgent, HttpServletResponse httpResponse) {
        String phoneNumber = PhoneNumberUtils.normalize(request.getPhoneNumber());

        rateLimitService.checkLoginLimit(phoneNumber);

        User user = userRepository.findByPhone(phoneNumber)
                .orElseThrow(() -> new BusinessException(ErrorCode.BAD_CREDENTIALS));
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BusinessException(ErrorCode.BAD_CREDENTIALS);
        }
        if (user.getStatus() == UserStatus.BLACK_LIST) {
            throw new BusinessException(ErrorCode.USER_BLACKLISTED, blacklistMessage());
        }
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new BusinessException(ErrorCode.USER_NOT_ACTIVE);
        }

        String jti = UUID.randomUUID().toString();
        String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), phoneNumber, jti);
        tokenService.storeAccessToken(jti, user.getId());
        String refreshToken = tokenService.createRefreshToken(user.getId(), jti);

        saveSessionLog(user.getId(), phoneNumber, AuthSessionAction.LOGIN, jti, requestIp, userAgent);
        Cookie cookie = new Cookie("accessToken", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(15*60);
        httpResponse.addCookie(cookie);
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresInSeconds(jwtTokenProvider.getAccessTokenExpirationMs() / 1000)
                .build();
    }

    @Transactional
    public void logout(String refreshToken, String requestIp, String userAgent) {
        TokenService.RevokedSession session = tokenService.revoke(refreshToken);
        String phoneNumber = userRepository.findById(session.getUserId())
                .map(User::getPhone).orElse(null);
        saveSessionLog(session.getUserId(), phoneNumber, AuthSessionAction.LOGOUT,
                session.getJti(), requestIp, userAgent);
    }

    private void saveSessionLog(Long userId, String phoneNumber, AuthSessionAction action,
                                String jti, String requestIp, String userAgent) {
        AuthSessionLog sessionLog = new AuthSessionLog();
        sessionLog.setUserId(userId);
        sessionLog.setPhoneNumber(phoneNumber);
        sessionLog.setAction(action);
        sessionLog.setJti(jti);
        sessionLog.setRequestIp(requestIp);
        sessionLog.setUserAgent(userAgent);
        authSessionLogRepository.save(sessionLog);
    }

    private String blacklistMessage() {
        return String.format("Vui long lien he so dien thoai %s de duoc ho tro.", supportHotline);
    }
}

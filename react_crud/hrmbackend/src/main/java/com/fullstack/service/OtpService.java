package com.fullstack.service;

import com.fullstack.entity.OtpChannel;
import com.fullstack.entity.OtpGenerationLog;
import com.fullstack.entity.OtpPurpose;
import com.fullstack.entity.OtpStatus;
import com.fullstack.entity.OtpVerification;
import com.fullstack.notification.entity.NotificationChannel;
import com.fullstack.repository.OtpGenerationLogRepository;
import com.fullstack.repository.OtpVerificationRepository;
import com.fullstack.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OtpService {

    /** OTP toi da 6 ky tu theo yeu cau */
    private static final int MAX_OTP_LENGTH = 6;

    private static final SecureRandom RANDOM = new SecureRandom();

    private final OtpVerificationRepository otpVerificationRepository;
    private final OtpGenerationLogRepository otpGenerationLogRepository;
    private final NotificationService notificationService;
    private final RateLimitService rateLimitService;

    @Value("${otp.expiration-seconds:300}")
    private long expirationSeconds;

    @Value("${otp.length:6}")
    private int otpLength;

    @Value("${otp.max-verify-attempts:5}")
    private int maxVerifyAttempts;

    /**
     * Sinh OTP moi (vo hieu hoa OTP PENDING cu), luu otp_verification,
     * ghi log chi tiet vao otp_generation_logs va mock gui qua outbox/log.
     */
    @Transactional
    public long generateAndSend(Long userId, String phoneNumber, OtpChannel channel, OtpPurpose purpose,
                                String requestIp, String userAgent) {
        rateLimitService.checkOtpRequestLimit(phoneNumber);

        otpVerificationRepository.expirePendingOtps(phoneNumber, purpose);

        String otpCode = generateCode();
        LocalDateTime expiresAt = LocalDateTime.now().plusSeconds(expirationSeconds);

        OtpVerification otp = new OtpVerification();
        otp.setUserId(userId);
        otp.setPhoneNumber(phoneNumber);
        otp.setChannel(channel);
        otp.setPurpose(purpose);
        otp.setOtpCode(otpCode);
        otp.setStatus(OtpStatus.PENDING);
        otp.setAttemptCount(0);
        otp.setExpiresAt(expiresAt);
        otpVerificationRepository.save(otp);

        OtpGenerationLog genLog = new OtpGenerationLog();
        genLog.setUserId(userId);
        genLog.setPhoneNumber(phoneNumber);
        genLog.setChannel(channel);
        genLog.setPurpose(purpose);
        genLog.setOtpCode(otpCode);
        genLog.setOtpExpiresAt(expiresAt);
        genLog.setRequestIp(requestIp);
        genLog.setUserAgent(userAgent);
        otpGenerationLogRepository.save(genLog);

        notificationService.sendOtp(
                channel == OtpChannel.EMAIL ? NotificationChannel.EMAIL : NotificationChannel.SMS,
                phoneNumber, otpCode, expirationSeconds);

        return expirationSeconds;
    }

    /**
     * Xac thuc OTP. Tra ve ket qua kem bo dem so lan sai (khong throw exception
     * de cac update attempt_count/status duoc commit); caller quyet dinh message.
     */
    @Transactional
    public OtpVerifyResult verify(String phoneNumber, OtpPurpose purpose, String otpCode) {
        OtpVerification otp = otpVerificationRepository
                .findTopByPhoneNumberAndPurposeAndStatusOrderByIdDesc(phoneNumber, purpose, OtpStatus.PENDING)
                .orElse(null);
        if (otp == null) {
            return new OtpVerifyResult(OtpVerifyResult.Outcome.NOT_FOUND, 0, maxVerifyAttempts);
        }

        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            otp.setStatus(OtpStatus.EXPIRED);
            otpVerificationRepository.save(otp);
            return new OtpVerifyResult(OtpVerifyResult.Outcome.EXPIRED, otp.getAttemptCount(), maxVerifyAttempts);
        }

        if (!otp.getOtpCode().equals(otpCode)) {
            otp.setAttemptCount(otp.getAttemptCount() + 1);
            boolean exhausted = otp.getAttemptCount() >= maxVerifyAttempts;
            if (exhausted) {
                otp.setStatus(OtpStatus.FAILED);
            }
            otpVerificationRepository.save(otp);
            return new OtpVerifyResult(
                    exhausted ? OtpVerifyResult.Outcome.EXHAUSTED : OtpVerifyResult.Outcome.WRONG,
                    otp.getAttemptCount(), maxVerifyAttempts);
        }

        otp.setStatus(OtpStatus.VERIFIED);
        otp.setVerifiedAt(LocalDateTime.now());
        otpVerificationRepository.save(otp);
        return new OtpVerifyResult(OtpVerifyResult.Outcome.SUCCESS, otp.getAttemptCount(), maxVerifyAttempts);
    }

    private String generateCode() {
        int length = Math.min(otpLength, MAX_OTP_LENGTH);
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }
}

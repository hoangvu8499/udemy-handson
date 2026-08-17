package com.fullstack.controller;

import com.fullstack.dto.LoginRequest;
import com.fullstack.dto.LogoutRequest;
import com.fullstack.dto.RegisterRequest;
import com.fullstack.dto.RegisterResponse;
import com.fullstack.dto.TokenResponse;
import com.fullstack.dto.VerifyOtpRequest;
import com.fullstack.service.AuthService;
import com.fullstack.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Tag(name = "Auth", description = "Dang ky, xac thuc OTP, login/logout")
@SecurityRequirements // API auth la public, khong yeu cau Bearer token
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Dang ky bang so dien thoai",
            description = "Tao user UNVERIFIED va gui OTP qua SMS (mock qua log/outbox).")
    @PostMapping("/register")
    public ApiResponse<RegisterResponse> register(@Valid @RequestBody RegisterRequest request,
                                                  HttpServletRequest httpRequest) {
        RegisterResponse response = authService.register(request,
                extractClientIp(httpRequest), httpRequest.getHeader("User-Agent"));
        return ApiResponse.success("OTP sent, please verify to activate your account", response);
    }

    @Operation(summary = "Xac thuc OTP",
            description = "OTP dung -> user chuyen ACTIVE. Moi lan sai tra ve bo dem so lan thu; "
                    + "sai du 5 lan thi user bi BLACK_LIST va phai lien he hotline.")
    @PostMapping("/verify-otp")
    public ApiResponse<Void> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        authService.verifyOtp(request);
        return ApiResponse.success("Account activated successfully", null);
    }

    @Operation(summary = "Login bang so dien thoai + password",
            description = "Tra ve JWT access token + refresh token; token duoc luu Redis de verify/thu hoi. "
                    + "Moi phien login/logout deu duoc ghi vao bang auth_session_logs.")
    @PostMapping("/login")
    public ApiResponse<TokenResponse> login(@Valid @RequestBody LoginRequest request,
                                            HttpServletRequest httpRequest) {
        return ApiResponse.success(authService.login(request,
                extractClientIp(httpRequest), httpRequest.getHeader("User-Agent")));
    }

    @Operation(summary = "Login bang so dien thoai + password",
            description = "Tra ve JWT access token + refresh token; token duoc luu Redis de verify/thu hoi. "
                    + "Moi phien login/logout deu duoc ghi vao bang auth_session_logs.")
    @PostMapping("/login2")
    public ApiResponse<TokenResponse> login2(@Valid @RequestBody LoginRequest request,
                                        HttpServletRequest httpRequest,
                                        HttpServletResponse httpResponse) {
        return ApiResponse.success(authService.login2(request,
                extractClientIp(httpRequest), 
                httpRequest.getHeader("User-Agent"),
                httpResponse));
    }

    @Operation(summary = "Logout — thu hoi refresh token",
            description = "Xoa refresh token + access token cung phien khoi Redis, ghi log LOGOUT.")
    @PostMapping("/logout")
    public ApiResponse<Void> logout(@Valid @RequestBody LogoutRequest request,
                                    HttpServletRequest httpRequest) {
        authService.logout(request.getRefreshToken(),
                extractClientIp(httpRequest), httpRequest.getHeader("User-Agent"));
        return ApiResponse.success("Logged out successfully", null);
    }

    private String extractClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(forwarded)) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
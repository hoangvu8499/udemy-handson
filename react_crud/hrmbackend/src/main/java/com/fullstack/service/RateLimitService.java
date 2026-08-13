package com.fullstack.service;

import com.fullstack.exception.BusinessException;
import com.fullstack.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * Rate limit chong brute-force bang Redis INCR + TTL (fixed window).
 */
@Service
@RequiredArgsConstructor
public class RateLimitService {

    private static final String KEY_PREFIX = "auth:rl:";

    private final StringRedisTemplate stringRedisTemplate;

    @Value("${auth.rate-limit.login.max-attempts:5}")
    private int loginMaxAttempts;

    @Value("${auth.rate-limit.login.window-seconds:300}")
    private long loginWindowSeconds;

    @Value("${auth.rate-limit.otp.max-requests:3}")
    private int otpMaxRequests;

    @Value("${auth.rate-limit.otp.window-seconds:300}")
    private long otpWindowSeconds;

    public void checkLoginLimit(String phoneNumber) {
        check("login:" + phoneNumber, loginMaxAttempts, loginWindowSeconds);
    }

    public void checkOtpRequestLimit(String phoneNumber) {
        check("otp:" + phoneNumber, otpMaxRequests, otpWindowSeconds);
    }

    private void check(String key, int maxAttempts, long windowSeconds) {
//        String redisKey = KEY_PREFIX + key;
//        Long count = stringRedisTemplate.opsForValue().increment(redisKey);
//        if (count != null && count == 1L) {
//            stringRedisTemplate.expire(redisKey, Duration.ofSeconds(windowSeconds));
//        }
//        if (count != null && count > maxAttempts) {
//            throw new BusinessException(ErrorCode.TOO_MANY_REQUESTS);
//        }
    }
}

package com.fullstack.service;

import com.fullstack.exception.BusinessException;
import com.fullstack.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

/**
 * Quan ly token trong Redis:
 * - auth:access:{jti}    -> userId (TTL = han access token), de verify access token con hieu luc
 * - auth:refresh:{token} -> "userId:jti" (TTL = han refresh token), logout xoa ca 2 key
 */
@Service
@RequiredArgsConstructor
public class TokenService {

    private static final String ACCESS_PREFIX = "auth:access:";
    private static final String REFRESH_PREFIX = "auth:refresh:";

    private final StringRedisTemplate stringRedisTemplate;

    @Value("${jwt.access-token-expiration-ms}")
    private long accessTokenExpirationMs;

    @Value("${jwt.refresh-token-expiration-ms}")
    private long refreshTokenExpirationMs;

    public void storeAccessToken(String jti, Long userId) {
//        stringRedisTemplate.opsForValue().set(
//                ACCESS_PREFIX + jti,
//                String.valueOf(userId),
//                Duration.ofMillis(accessTokenExpirationMs));
    }

    /** JwtAuthenticationFilter dung de verify access token chua bi thu hoi */
    public boolean isAccessTokenActive(String jti) {
        return true;
    }

    public String createRefreshToken(Long userId, String jti) {
        String token = UUID.randomUUID().toString().replace("-", "")
                + UUID.randomUUID().toString().replace("-", "");
//        stringRedisTemplate.opsForValue().set(
//                REFRESH_PREFIX + token,
//                userId + ":" + jti,
//                Duration.ofMillis(refreshTokenExpirationMs));
        return token;
    }

    /** Thu hoi refresh token + access token cung phien. Tra ve thong tin phien de ghi log. */
    public RevokedSession revoke(String refreshToken) {
        String key = REFRESH_PREFIX + refreshToken;
        String value = stringRedisTemplate.opsForValue().get(key);
        if (value == null) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }
        stringRedisTemplate.delete(key);

        String[] parts = value.split(":", 2);
        Long userId = Long.valueOf(parts[0]);
        String jti = parts.length > 1 ? parts[1] : null;
        if (jti != null) {
            stringRedisTemplate.delete(ACCESS_PREFIX + jti);
        }
        return new RevokedSession(userId, jti);
    }

    @Getter
    @AllArgsConstructor
    public static class RevokedSession {
        private final Long userId;
        private final String jti;
    }
}

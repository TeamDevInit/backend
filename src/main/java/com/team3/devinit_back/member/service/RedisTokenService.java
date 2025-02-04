package com.team3.devinit_back.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisTokenService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void saveRefreshToken(String socialId, String refreshToken, long expirationMs) {
        redisTemplate.opsForValue().set(
                "refreshToken:" + socialId,
                refreshToken,
                expirationMs,
                TimeUnit.MILLISECONDS
        );
    }

    public String getRefreshToken(String socialId) {
        return (String) redisTemplate.opsForValue().get("refreshToken:" + socialId);
    }

    public void deleteRefreshToken(String socialId) {
        redisTemplate.delete("refreshToken:" + socialId);
    }

    public void createBlacklist(String accessToken, long expirationMs){
        redisTemplate.opsForValue().set(
                "blacklist:" + accessToken,
                "BLOCKED",
                expirationMs,
                TimeUnit.MILLISECONDS
        );
    }
    public boolean isBlacklisted(String accessToken) {
        return redisTemplate.hasKey("blacklist:" + accessToken);
    }

}

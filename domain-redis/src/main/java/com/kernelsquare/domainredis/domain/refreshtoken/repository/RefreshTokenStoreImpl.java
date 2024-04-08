package com.kernelsquare.domainredis.domain.refreshtoken.repository;

import com.kernelsquare.domainredis.domain.refreshtoken.entity.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.kernelsquare.domainredis.constants.RedisConstans.REFRESHTOKEN_PREFIX;
import static com.kernelsquare.domainredis.constants.RedisConstans.RefreshTokenTTL;

@Component
@RequiredArgsConstructor
public class RefreshTokenStoreImpl implements RefreshTokenStore {
    private final RedisTemplate<String, RefreshToken> redisTemplate;

    @Override
    public void delete(RefreshToken refreshToken) {
        redisTemplate.opsForValue().getOperations().delete(REFRESHTOKEN_PREFIX + refreshToken.getMemberId());
    }

    @Override
    public void store(RefreshToken refreshToken) {
        redisTemplate.opsForValue().set(REFRESHTOKEN_PREFIX + refreshToken.getMemberId(), refreshToken, RefreshTokenTTL, TimeUnit.SECONDS);
    }
}

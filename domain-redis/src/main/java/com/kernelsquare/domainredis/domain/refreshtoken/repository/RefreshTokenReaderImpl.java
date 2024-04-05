package com.kernelsquare.domainredis.domain.refreshtoken.repository;

import com.kernelsquare.domainredis.domain.refreshtoken.entity.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import static com.kernelsquare.domainredis.constants.RedisConstans.REFRESHTOKEN_PREFIX;

@Component
@RequiredArgsConstructor
public class RefreshTokenReaderImpl implements RefreshTokenReader {
    private final RedisTemplate<String, RefreshToken> redisTemplate;

    @Override
    public RefreshToken find(String memberId) {
        return redisTemplate.opsForValue().get(REFRESHTOKEN_PREFIX + memberId);
    }
}

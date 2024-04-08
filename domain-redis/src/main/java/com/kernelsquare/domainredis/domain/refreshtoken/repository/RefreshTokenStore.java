package com.kernelsquare.domainredis.domain.refreshtoken.repository;

import com.kernelsquare.domainredis.domain.refreshtoken.entity.RefreshToken;

public interface RefreshTokenStore {
    void delete(RefreshToken refreshToken);

    void store(RefreshToken refreshToken);
}

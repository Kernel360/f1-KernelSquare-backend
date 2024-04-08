package com.kernelsquare.domainredis.domain.refreshtoken.repository;

import com.kernelsquare.domainredis.domain.refreshtoken.entity.RefreshToken;

public interface RefreshTokenReader {
    RefreshToken find(String memberId);
}

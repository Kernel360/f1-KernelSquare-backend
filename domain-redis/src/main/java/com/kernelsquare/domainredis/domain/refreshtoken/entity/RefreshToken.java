package com.kernelsquare.domainredis.domain.refreshtoken.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RefreshToken {
    private String memberId;

    private String refreshToken;

    private LocalDateTime createdDate;

    private LocalDateTime expirationDate;

    @Builder
    public RefreshToken(String memberId, String refreshToken, LocalDateTime createdDate, LocalDateTime expirationDate) {
        this.memberId = memberId;
        this.refreshToken = refreshToken;
        this.createdDate = createdDate;
        this.expirationDate = expirationDate;
    }
}

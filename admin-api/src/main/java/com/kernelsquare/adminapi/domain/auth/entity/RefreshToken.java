package com.kernelsquare.adminapi.domain.auth.entity;

import java.time.LocalDateTime;

import org.springframework.data.redis.core.RedisHash;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "refreshToken", timeToLive = 60)
public class RefreshToken {
	@Id
	private Long memberId;

	private String refreshToken;

	private LocalDateTime createdDate;

	private LocalDateTime expirationDate;

	@Builder
	public RefreshToken(Long memberId, String refreshToken, LocalDateTime createdDate, LocalDateTime expirationDate) {
		this.memberId = memberId;
		this.refreshToken = refreshToken;
		this.createdDate = createdDate;
		this.expirationDate = expirationDate;
	}
}

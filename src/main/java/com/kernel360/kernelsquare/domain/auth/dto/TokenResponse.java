package com.kernel360.kernelsquare.domain.auth.dto;

import lombok.Builder;

@Builder
public record TokenResponse(
	String accessToken,
	String refreshToken) {

	public static TokenResponse of(
		String accessToken,
		String refreshToken
	) {
		return TokenResponse
			.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}
}
package com.kernelsquare.adminapi.domain.auth.dto;

import lombok.Builder;

@Builder
public record TokenRequest(
	String accessToken,
	String refreshToken) {

	public static TokenRequest of(
		String accessToken,
		String refreshToken
	) {
		return TokenRequest
			.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}
}

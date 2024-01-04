package com.kernel360.kernelsquare.domain.auth.dto;

import lombok.Builder;

@Builder
public record TokenDto(
	String accessToken,
	String refreshToken) {

	public static TokenDto of(
		String accessToken,
		String refreshToken
	) {
		return TokenDto
			.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}
}

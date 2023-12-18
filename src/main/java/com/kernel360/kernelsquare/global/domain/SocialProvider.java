package com.kernel360.kernelsquare.global.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialProvider {
	KAKAO("kakao"),
	GITHUB("github"),
	GOOGLE("google");

	private final String description;
}

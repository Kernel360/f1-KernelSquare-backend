package com.kernel360.kernelsquare.domain.auth.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import lombok.Builder;

@Builder
public record LoginResponse(
	String nickname,
	List<String> roles,
	TokenDto tokenDto
) {

	public static LoginResponse of(String nickname, Collection<? extends GrantedAuthority> authorities,
		TokenDto tokenDto) {
		List<String> roles = authorities.stream()
			.map(String::valueOf)
			.toList();

		return LoginResponse.builder()
			.nickname(nickname)
			.roles(roles)
			.tokenDto(tokenDto)
			.build();
	}
}

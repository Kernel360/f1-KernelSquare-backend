package com.kernel360.kernelsquare.domain.auth.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.kernel360.kernelsquare.domain.member.entity.Member;

import lombok.Builder;

@Builder
public record LoginResponse(
	Long memberId,
	String nickname,
	Long experience,
	String introduction,
	String imageUrl,
	Long level,
	List<String> roles,
	TokenDto tokenDto
) {

	public static LoginResponse of(Member member, Collection<? extends GrantedAuthority> authorities,
		TokenDto tokenDto) {
		List<String> roles = authorities.stream()
			.map(String::valueOf)
			.toList();

		return LoginResponse.builder()
			.memberId(member.getId())
			.nickname(member.getNickname())
			.experience(member.getExperience())
			.introduction(member.getIntroduction())
			.imageUrl(member.getImageUrl())
			.level(member.getLevel().getName())
			.roles(roles)
			.tokenDto(tokenDto)
			.build();
	}
}

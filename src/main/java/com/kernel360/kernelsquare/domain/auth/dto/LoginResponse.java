package com.kernel360.kernelsquare.domain.auth.dto;

import java.util.List;

import com.kernel360.kernelsquare.domain.authority.entity.Authority;
import com.kernel360.kernelsquare.domain.image.utils.ImageUtils;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.member_authority.entity.MemberAuthority;

import com.kernel360.kernelsquare.global.domain.AuthorityType;
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
	TokenResponse tokenDto

) {

	public static LoginResponse of(Member member, TokenResponse tokenResponse) {
		List<String> roles = member.getAuthorities().stream()
			.map(MemberAuthority::getAuthority)
			.map(Authority::getAuthorityType)
			.map(AuthorityType::getDescription)
			.toList();

		return LoginResponse.builder()
			.memberId(member.getId())
			.nickname(member.getNickname())
			.experience(member.getExperience())
			.introduction(member.getIntroduction())
			.imageUrl(ImageUtils.makeImageUrl(member.getImageUrl()))
			.level(member.getLevel().getName())
			.roles(roles)
			.tokenDto(tokenResponse)
			.build();
	}
}

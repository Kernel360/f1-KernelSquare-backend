package com.kernelsquare.memberapi.domain.auth.dto;

import java.util.List;

import com.kernelsquare.core.util.ImageUtils;
import com.kernelsquare.core.type.AuthorityType;
import com.kernelsquare.domainmysql.domain.authority.entity.Authority;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member_authority.entity.MemberAuthority;

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

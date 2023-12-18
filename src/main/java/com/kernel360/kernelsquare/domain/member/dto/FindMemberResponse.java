package com.kernel360.kernelsquare.domain.member.dto;

import java.util.List;

import com.kernel360.kernelsquare.domain.authority.entity.Authority;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.member_authority.entity.MemberAuthority;

public record FindMemberResponse(
	String nickname,
	Long experience,
	String introduction,
	List<Authority> authorities,
	String imageUrl
) {

	public static FindMemberResponse from(Member member) {
		return new FindMemberResponse(
			member.getNickname(),
			member.getExperience(),
			member.getIntroduction(),
			member.getAuthorities()
				.stream()
				.map(MemberAuthority::getAuthority)
				.toList(),
			member.getImageUrl());
	}
}

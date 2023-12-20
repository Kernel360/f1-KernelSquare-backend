package com.kernel360.kernelsquare.domain.member.dto;

import com.kernel360.kernelsquare.domain.member.entity.Member;

public record FindMemberResponse(
	String nickname,
	Long experience,
	String introduction,
	String imageUrl
) {

	public static FindMemberResponse from(Member member) {
		return new FindMemberResponse(
			member.getNickname(),
			member.getExperience(),
			member.getIntroduction(),
			member.getImageUrl());
	}
}

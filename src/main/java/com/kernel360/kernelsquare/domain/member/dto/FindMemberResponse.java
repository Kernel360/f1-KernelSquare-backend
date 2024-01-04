package com.kernel360.kernelsquare.domain.member.dto;

import com.kernel360.kernelsquare.domain.member.entity.Member;

import lombok.Builder;

@Builder
public record FindMemberResponse(
	Long memberId,
	String nickname,
	Long experience,
	String introduction,
	String imageUrl,

	Long level
) {

	public static FindMemberResponse from(Member member) {
		return FindMemberResponse
			.builder()
			.memberId(member.getId())
			.nickname(member.getNickname())
			.experience(member.getExperience())
			.introduction(member.getIntroduction())
			.imageUrl(member.getImageUrl())
			.level(member.getLevel().getName())
			.build();
	}
}



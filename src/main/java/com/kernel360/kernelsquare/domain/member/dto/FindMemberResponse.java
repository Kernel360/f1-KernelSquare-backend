package com.kernel360.kernelsquare.domain.member.dto;

import com.kernel360.kernelsquare.domain.member.entity.Member;

import lombok.Builder;

@Builder
public record FindMemberResponse(
	Long id,
	String nickname,
	Long experience,
	String introduction,
	String imageUrl
) {

	public static FindMemberResponse from(Member member) {
		return FindMemberResponse
			.builder()
			.id(member.getId())
			.nickname(member.getNickname())
			.experience(member.getExperience())
			.introduction(member.getIntroduction())
			.imageUrl(member.getImageUrl())
			.build();
	}
}



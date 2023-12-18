package com.kernel360.kernelsquare.domain.member.dto;

import com.kernel360.kernelsquare.domain.member.entity.Member;

public record UpdateMemberResponse(
	String imageUrl) {

	public static UpdateMemberResponse from(Member member) {
		return new UpdateMemberResponse(
			member.getImageUrl());
	}
}

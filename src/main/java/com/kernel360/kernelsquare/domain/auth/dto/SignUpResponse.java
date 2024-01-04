package com.kernel360.kernelsquare.domain.auth.dto;

import com.kernel360.kernelsquare.domain.member.entity.Member;

public record SignUpResponse(
	Long memberId
) {

	public static SignUpResponse of(Member member) {
		return new SignUpResponse(member.getId());
	}
}

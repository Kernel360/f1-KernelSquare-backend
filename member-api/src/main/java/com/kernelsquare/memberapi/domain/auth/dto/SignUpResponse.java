package com.kernelsquare.memberapi.domain.auth.dto;

import com.kernelsquare.domainmysql.domain.member.entity.Member;

public record SignUpResponse(
	Long memberId
) {

	public static SignUpResponse of(Member member) {
		return new SignUpResponse(member.getId());
	}
}

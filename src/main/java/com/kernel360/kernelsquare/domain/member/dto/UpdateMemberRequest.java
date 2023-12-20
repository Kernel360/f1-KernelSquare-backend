package com.kernel360.kernelsquare.domain.member.dto;

public record UpdateMemberRequest(
	String imageUrl,
	String introduction
) {
}

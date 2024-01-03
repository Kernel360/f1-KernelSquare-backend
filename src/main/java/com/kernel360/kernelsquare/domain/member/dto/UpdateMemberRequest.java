package com.kernel360.kernelsquare.domain.member.dto;

import lombok.Builder;

@Builder
public record UpdateMemberRequest(
	String imageUrl,
	String introduction
) {
}

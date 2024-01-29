package com.kernel360.kernelsquare.domain.member.dto;

import lombok.Builder;

@Builder
public record UpdateMemberProfileRequest(
	String imageUrl
) {
}

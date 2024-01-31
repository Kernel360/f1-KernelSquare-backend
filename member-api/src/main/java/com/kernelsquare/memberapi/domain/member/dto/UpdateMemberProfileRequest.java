package com.kernelsquare.memberapi.domain.member.dto;

import lombok.Builder;

@Builder
public record UpdateMemberProfileRequest(
	String imageUrl
) {
}

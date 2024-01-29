package com.kernel360.kernelsquare.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateMemberIntroductionRequest(
	@NotBlank
	String introduction
) {
}

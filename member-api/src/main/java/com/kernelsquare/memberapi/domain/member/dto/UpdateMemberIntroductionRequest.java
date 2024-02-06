package com.kernelsquare.memberapi.domain.member.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateMemberIntroductionRequest(
	@NotNull(message = "자기 소개글 수정은 null일 수 없습니다.")
	String introduction
) {
}

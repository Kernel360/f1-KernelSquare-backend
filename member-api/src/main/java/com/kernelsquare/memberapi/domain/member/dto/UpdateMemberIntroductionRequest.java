package com.kernelsquare.memberapi.domain.member.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateMemberIntroductionRequest(
	@NotNull(message = "자기 소개글 수정은 null일 수 없습니다.")
	@Size(max = 1000, message = "자기 소개글은 1000자를 넘을 수 없습니다.")
	String introduction
) {
}

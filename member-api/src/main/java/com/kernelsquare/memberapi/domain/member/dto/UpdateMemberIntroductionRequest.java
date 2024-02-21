package com.kernelsquare.memberapi.domain.member.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateMemberIntroductionRequest(
	@NotNull(message = "자기 소개글 수정은 null일 수 없습니다.")
	@Size(min = 10, max = 1000, message = "자기 소개글은 10자 이상 1000자 이하로 작성해 주세요.")
	String introduction
) {
}

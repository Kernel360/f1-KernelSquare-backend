package com.kernelsquare.memberapi.domain.answer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateAnswerRequest(
	@NotBlank(message = "답변 내용을 입력해 주세요.")
	@Size(max = 10000, message = "답변 내용은 10000자를 넘을 수 없습니다.")
	String content,
	String imageUrl
) {
}
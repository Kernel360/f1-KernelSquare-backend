package com.kernel360.kernelsquare.domain.answer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateAnswerRequest(
	@NotBlank(message = "답변 내용을 입력해 주세요.")
	String content,
	String imageUrl
) {
}
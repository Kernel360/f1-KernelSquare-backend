package com.kernel360.kernelsquare.domain.answer.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateAnswerRequest(
	@NotBlank(message = "답변 내용은 필수 입력값입니다.")
	String content,
	String imageUrl
) {
}
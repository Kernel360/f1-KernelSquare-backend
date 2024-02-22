package com.kernelsquare.memberapi.domain.answer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateAnswerRequest(
	@NotBlank(message = "답변 내용을 입력해 주세요.")
	@Size(min = 10, max = 10000, message = "답변 내용은 10자 이상 10000자 이하로 작성해 주세요.")
	String content,
	String imageUrl
) {
}
package com.kernelsquare.memberapi.domain.question.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateQuestionRequest(
	@NotBlank(message = "질문 제목을 입력해 주세요.")
	String title,
	@NotBlank(message = "질문 내용을 입력해 주세요.")
	String content,
	String imageUrl,
	@NotNull(message = "최소 빈 리스트로 입력해 주세요.")
	List<String> skills
) {
}

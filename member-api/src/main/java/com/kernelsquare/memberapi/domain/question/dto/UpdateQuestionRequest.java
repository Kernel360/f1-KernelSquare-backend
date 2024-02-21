package com.kernelsquare.memberapi.domain.question.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateQuestionRequest(
	@NotBlank(message = "질문 제목을 입력해 주세요.")
	@Size(min = 5, max = 100, message = "질문 제목은 5자 이상 100자 이하로 작성해 주세요.")
	String title,
	@NotBlank(message = "질문 내용을 입력해 주세요.")
	@Size(min = 10, max = 10000, message = "질문 내용은 10자 이상 10000자 이하로 작성해 주세요.")
	String content,
	String imageUrl,
	@NotNull(message = "최소 빈 리스트로 입력해 주세요.")
	List<String> skills
) {
}

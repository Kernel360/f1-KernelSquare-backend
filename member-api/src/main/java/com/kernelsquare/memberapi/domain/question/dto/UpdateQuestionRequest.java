package com.kernelsquare.memberapi.domain.question.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateQuestionRequest(
	@NotBlank(message = "질문 제목을 입력해 주세요.")
	@Size(max = 100, message = "질문 제목은 100자를 넘을 수 없습니다.")
	String title,
	@NotBlank(message = "질문 내용을 입력해 주세요.")
	@Size(max = 10000, message = "질문 내용은 10000자를 넘을 수 없습니다.")
	String content,
	String imageUrl,
	@NotNull(message = "최소 빈 리스트로 입력해 주세요.")
	List<String> skills
) {
}

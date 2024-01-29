package com.kernel360.kernelsquare.domain.question.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record UpdateQuestionRequest(
	@NotBlank(message = "질문 제목을 입력해 주세요.")
	String title,
	@NotBlank(message = "질문 내용을 입력해 주세요.")
	String content,
	String imageUrl,
	@NotNull(message = "최소 빈 리스트로 입력해 주세요.")
	List<String> skills
) {}

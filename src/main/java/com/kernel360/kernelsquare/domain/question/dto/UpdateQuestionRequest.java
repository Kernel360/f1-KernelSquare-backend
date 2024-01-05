package com.kernel360.kernelsquare.domain.question.dto;

import java.util.List;

import com.kernel360.kernelsquare.domain.question.entity.Question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateQuestionRequest(
	@NotBlank(message = "빈 제목으로 수정할 수 없습니다.")
	String title,
	@NotBlank(message = "빈 내용으로 수정할 수 없습니다.")
	String content,
	String imageUrl,
	@NotNull(message = "최소한 빈 리스트로 들어와야 합니다.")
	List<String> skills
) {
	public static Question toEntity(UpdateQuestionRequest updateQuestionRequest) {
		return Question.builder()
			.title(updateQuestionRequest.title())
			.content(updateQuestionRequest.content())
			.imageUrl(updateQuestionRequest.imageUrl())
			.build();
	}
}

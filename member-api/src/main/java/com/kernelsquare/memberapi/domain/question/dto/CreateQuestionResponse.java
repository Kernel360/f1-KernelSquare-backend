package com.kernelsquare.memberapi.domain.question.dto;

import com.kernelsquare.domainmysql.domain.question.entity.Question;

public record CreateQuestionResponse(
	Long questionId
) {
	public static CreateQuestionResponse from(Question question) {
		return new CreateQuestionResponse(
			question.getId()
		);
	}
}

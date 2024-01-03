package com.kernel360.kernelsquare.domain.question.dto;

import com.kernel360.kernelsquare.domain.question.entity.Question;

public record CreateQuestionResponse(
    Long questionId
) {
    public static CreateQuestionResponse from(Question question) {
        return new CreateQuestionResponse(
            question.getId()
        );
    }
}

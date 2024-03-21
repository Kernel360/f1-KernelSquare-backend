package com.kernelsquare.memberapi.domain.question.dto;

public record FindQuestionIdResponse(
        Long questionId
) {
    public static FindQuestionIdResponse of(Long questionId) {
        return new FindQuestionIdResponse(
                questionId
        );
    }
}

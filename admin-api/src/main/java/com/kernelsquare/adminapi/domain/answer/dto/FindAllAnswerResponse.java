package com.kernelsquare.adminapi.domain.answer.dto;

import java.util.List;

public record FindAllAnswerResponse (
        List<FindAnswerResponse> answerResponses
) {
    public static FindAllAnswerResponse from(List<FindAnswerResponse> findAnswerResponses) {
        return new FindAllAnswerResponse(
                findAnswerResponses
        );
    }
}

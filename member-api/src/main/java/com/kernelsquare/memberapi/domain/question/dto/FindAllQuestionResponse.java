package com.kernelsquare.memberapi.domain.question.dto;

import java.util.List;

public record FindAllQuestionResponse(
        List<FindQuestionIdResponse> questionIdList
) {

    public static FindAllQuestionResponse of(List<FindQuestionIdResponse> questionIdList) {
        return new FindAllQuestionResponse(
                questionIdList
        );
    }
}


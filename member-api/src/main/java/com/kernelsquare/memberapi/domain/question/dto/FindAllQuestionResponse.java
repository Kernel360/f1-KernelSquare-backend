package com.kernelsquare.memberapi.domain.question.dto;

import com.kernelsquare.domainmysql.domain.question.entity.Question;

import java.io.Serializable;

public record FindAllQuestionResponse(
        Long id
) implements Serializable {

    public static FindAllQuestionResponse of(Question question) {
        return new FindAllQuestionResponse(
                question.getId()
        );
    }
}

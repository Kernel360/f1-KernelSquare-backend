package com.kernel360.kernelsquare.global.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AnswerServiceStatus implements ServiceStatus{
    ANSWER_CREATION_NOT_AUTHORIZED(2120),
    ANSWER_UPDATE_NOT_AUTHORIZED(2121),

    ANSWER_CREATED(2150),
    ANSWERS_ALL_FOUND(2151),
    ANSWER_UPDATED(2152),
    ANSWER_DELETED(2153),
    VOTE_CREATED(2154),
    VOTE_DELETED(2155);

    private final Integer code;

    @Override
    public Integer getServiceStatus() {
        return code;
    }
}

package com.kernel360.kernelsquare.global.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AnswerServiceStatus implements ServiceStatus{
    ANSWER_CREATION_NOT_AUTHORIZED(2200),
    ANSWER_UPDATE_NOT_AUTHORIZED(2201),

    ANSWER_CREATED(2240),
    ANSWERS_ALL_FOUND(2241),
    ANSWER_UPDATED(2242),
    ANSWER_DELETED(2243),
    VOTE_CREATED(2244),
    VOTE_DELETED(2245);

    private final Integer code;

    @Override
    public Integer getServiceStatus() {
        return code;
    }
}

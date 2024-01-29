package com.kernel360.kernelsquare.global.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MemberAnswerVoteStatus implements ServiceStatus{
    MEMBER_ANSWER_VOTE_NOT_FOUND(2202),
    MEMBER_ANSWER_VOTE_CREATED(2244),
    MEMBER_ANSWER_VOTE_DELETED(2245);

    private final Integer code;

    @Override
    public Integer getServiceStatus() {
        return code;
    }
}

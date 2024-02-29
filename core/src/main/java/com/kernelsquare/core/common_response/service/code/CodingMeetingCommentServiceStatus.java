package com.kernelsquare.core.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CodingMeetingCommentServiceStatus implements ServiceStatus {
    //error
    CODING_MEETING_COMMENT_NOT_FOUND(5200),

    //success
    CODING_MEETING_COMMENT_ALL_FOUND(5240),
    CODING_MEETING_COMMENT_CREATED(5243),
    CODING_MEETING_COMMENT_UPDATED(5242),
    CODIMG_MEETING_COMMENT_DELETED(5241);

    private final Integer code;

    @Override
    public Integer getServiceStatus() {return code;}
}

package com.kernelsquare.core.common_response.error.code;

import com.kernelsquare.core.common_response.service.code.CodingMeetingCommentServiceStatus;
import com.kernelsquare.core.common_response.service.code.ServiceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CodingMeetingCommentErrorCode implements ErrorCode{
    CODIMG_MEETING_COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, CodingMeetingCommentServiceStatus.CODING_MEETING_COMMENT_NOT_FOUND,
            "모각코 댓글을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final ServiceStatus serviceStatus;
    private final String msg;

    @Override
    public HttpStatus getStatus() {
        return httpStatus;
    }

    @Override
    public Integer getCode() {
        return serviceStatus.getServiceStatus();
    }

    @Override
    public String getMsg() {
        return msg;
    }
}

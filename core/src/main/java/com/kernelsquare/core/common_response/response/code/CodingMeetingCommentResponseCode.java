package com.kernelsquare.core.common_response.response.code;

import com.kernelsquare.core.common_response.service.code.CodingMeetingCommentServiceStatus;
import com.kernelsquare.core.common_response.service.code.ServiceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CodingMeetingCommentResponseCode implements ResponseCode {
    CODING_MEETING_COMMENT_ALL_FOUND(HttpStatus.OK, CodingMeetingCommentServiceStatus.CODING_MEETING_COMMENT_ALL_FOUND, "모든 모각코 댓글 조회 성공"),
    CODING_MEETING_COMMENT_CREATED(HttpStatus.OK, CodingMeetingCommentServiceStatus.CODING_MEETING_COMMENT_CREATED, "모각코 댓글 생성 성공"),
    CODING_MEETING_COMMENT_UPDATED(HttpStatus.OK, CodingMeetingCommentServiceStatus.CODING_MEETING_COMMENT_UPDATED, "모각코 댓글 수정 성공"),
    CODING_MEETING_COMMENT_DELETED(HttpStatus.OK, CodingMeetingCommentServiceStatus.CODIMG_MEETING_COMMENT_DELETED, "모각코 댓글 삭제 성공");

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

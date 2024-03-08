package com.kernelsquare.core.common_response.error.code;

import com.kernelsquare.core.common_response.service.code.CodingMeetingServiceStatus;
import com.kernelsquare.core.common_response.service.code.ServiceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CodingMeetingErrorCode implements ErrorCode {
    CODING_MEETING_NOT_FOUND(HttpStatus.NOT_FOUND, CodingMeetingServiceStatus.CODING_MEETING_NOT_FOUND,
            "모각코 게시글을 찾을 수 없습니다."),
    CODING_MEETING_ALREADY_EXIST(HttpStatus.BAD_REQUEST, CodingMeetingServiceStatus.CODING_MEETING_ALREADY_EXIST,
            "진행중인 모각코가 이미 존재합니다."),
    FILTER_PARAMETER_NOT_VALID(HttpStatus.BAD_REQUEST, CodingMeetingServiceStatus.FILTER_PARAMETER_NOT_VALID,
            "유효하지 않은 필터 파라미터 입니다."),
    MEMBER_ID_IS_NULL(HttpStatus.BAD_REQUEST, CodingMeetingServiceStatus.MEMBER_ID_IS_NULL,
            "회원의 ID가 존재하지 않습니다.");

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

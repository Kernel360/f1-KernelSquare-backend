package com.kernelsquare.core.common_response.response.code;

import com.kernelsquare.core.common_response.service.code.CodingMeetingServiceStatus;
import com.kernelsquare.core.common_response.service.code.ServiceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CodingMeetingResponseCode implements ResponseCode{
    CODING_MEETING_FOUND(HttpStatus.OK, CodingMeetingServiceStatus.CODING_MEETING_FOUND, "모각코 조회 성공"),
    CODING_MEETING_ALL_FOUND(HttpStatus.OK, CodingMeetingServiceStatus.CODING_MEETING_ALL_FOUND, "모각코 전체 조회 성공"),
    CODING_MEETING_UPDATED(HttpStatus.OK, CodingMeetingServiceStatus.CODING_MEETING_UPDATED, "모각코 수정 성공"),
    CODING_MEETING_DELETED(HttpStatus.OK, CodingMeetingServiceStatus.CODING_MEETING_DELETED, "모각코 삭제 성공"),
    CODING_MEETING_CREATED(HttpStatus.OK, CodingMeetingServiceStatus.CODING_MEETING_CREATED, "모각코 생성 성공"),
    CODING_MEETING_CLOSED(HttpStatus.OK, CodingMeetingServiceStatus.CODING_MEETING_CLOSED, "모각코 마감 성공");

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

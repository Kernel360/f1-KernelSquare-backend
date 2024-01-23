package com.kernel360.kernelsquare.global.common_response.response.code;

import com.kernel360.kernelsquare.global.common_response.service.code.HashTagServiceStatus;
import com.kernel360.kernelsquare.global.common_response.service.code.ServiceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum HashTagResponseCode implements ResponseCode{
    HASHTAG_ALL_FOUND(HttpStatus.OK, HashTagServiceStatus.HASHTAG_ALL_FOUND, "모든 해시태그를 조회했습니다."),
    HASHTAG_DELETED(HttpStatus.OK, HashTagServiceStatus.HASHTAG_DELETED, "해시태그를 삭제했습니다.");

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

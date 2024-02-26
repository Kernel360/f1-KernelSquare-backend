package com.kernelsquare.core.common_response.response.code;

import com.kernelsquare.core.common_response.service.code.ServiceStatus;
import com.kernelsquare.core.common_response.service.code.StreamServiceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum StreamResponseCode implements ResponseCode {
    SSE_SUBSCRIBED(HttpStatus.OK, StreamServiceStatus.SSE_SUBSCRIBED, "SSE 구독 성공");

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
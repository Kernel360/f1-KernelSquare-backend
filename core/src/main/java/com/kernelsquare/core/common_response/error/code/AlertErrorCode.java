package com.kernelsquare.core.common_response.error.code;

import com.kernelsquare.core.common_response.service.code.AlertServiceStatus;
import com.kernelsquare.core.common_response.service.code.ServiceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum AlertErrorCode implements ErrorCode {
    EMITTER_NOT_FOUND(HttpStatus.NOT_FOUND, AlertServiceStatus.EMITTER_NOT_FOUND, "존재하지 않는 Emitter");

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

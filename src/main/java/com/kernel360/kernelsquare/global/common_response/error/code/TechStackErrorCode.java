package com.kernel360.kernelsquare.global.common_response.error.code;

import com.kernel360.kernelsquare.global.common_response.service.code.ServiceStatus;
import com.kernel360.kernelsquare.global.common_response.service.code.TechStackServiceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum TechStackErrorCode implements ErrorCode {
    TECH_STACK_NOT_FOUND(HttpStatus.NOT_FOUND, TechStackServiceStatus.TECH_STACK_NOT_FOUND, "존재하지 않는 기술 스택");

    private final HttpStatus code;
    private final ServiceStatus serviceStatus;
    private final String msg;

    @Override
    public HttpStatus getStatus() {
        return code;
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

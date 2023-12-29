package com.kernel360.kernelsquare.global.common_response.response.code;

import com.kernel360.kernelsquare.global.common_response.service.code.QuestionServiceStatus;
import com.kernel360.kernelsquare.global.common_response.service.code.ServiceStatus;
import com.kernel360.kernelsquare.global.common_response.service.code.TechStackServiceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum TechStackResponseCode implements ResponseCode {
    TECH_STACK_CREATED(HttpStatus.OK, TechStackServiceStatus.TECH_STACK_CREATED,"기슬 스택 생성 성공");

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
package com.kernel360.kernelsquare.global.common_response.response.code;

import com.kernel360.kernelsquare.global.common_response.service.code.LevelServiceStatus;
import com.kernel360.kernelsquare.global.common_response.service.code.ServiceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum LevelResponseCode implements ResponseCode {
    LEVEL_CREATED(HttpStatus.OK, LevelServiceStatus.LEVEL_CREATED, "등급 생성 성공");

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

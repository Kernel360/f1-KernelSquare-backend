package com.kernel360.kernelsquare.global.common_response.error.code;

import com.kernel360.kernelsquare.global.common_response.service.code.CommonServiceStatus;
import com.kernel360.kernelsquare.global.common_response.service.code.ServiceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode{
    DUPLICATE_DATA_EXIST(HttpStatus.CONFLICT, CommonServiceStatus.DUPLICATE_DATA_EXIST, "데이터 중복입니다");

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

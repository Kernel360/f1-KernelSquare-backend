package com.kernelsquare.core.common_response.response.code;

import com.kernelsquare.core.common_response.service.code.AlertServiceStatus;
import com.kernelsquare.core.common_response.service.code.ServiceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum AlertResponseCode implements ResponseCode {
    MY_ALERT_ALL_FOUND(HttpStatus.OK, AlertServiceStatus.MY_ALERT_ALL_FOUND, "나의 알림 모두 조회 성공");

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

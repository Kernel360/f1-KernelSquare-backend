package com.kernelsquare.core.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AlertServiceStatus implements ServiceStatus {
    // success
    MY_ALERT_ALL_FOUND(6100);

    private final Integer code;

    @Override
    public Integer getServiceStatus() {
        return code;
    }
}

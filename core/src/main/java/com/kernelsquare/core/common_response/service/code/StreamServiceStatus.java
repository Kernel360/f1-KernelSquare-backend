package com.kernelsquare.core.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum StreamServiceStatus implements ServiceStatus {
    //success
    SSE_SUBSCRIBED(5000);

    private final Integer code;

    @Override
    public Integer getServiceStatus() {
        return code;
    }
}
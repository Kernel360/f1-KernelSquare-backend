package com.kernelsquare.core.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AlertServiceStatus implements ServiceStatus {
    // success
    SSE_SUBSCRIBED(6100),
    MY_ALERT_ALL_FOUND(6101),

    // error
    EMITTER_NOT_FOUND(6140);

    private final Integer code;

    @Override
    public Integer getServiceStatus() {
        return code;
    }
}

package com.kernel360.kernelsquare.global.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CommonServiceStatus implements ServiceStatus {
    DUPLICATE_DATA_EXIST(9000);

    private final Integer code;

    @Override
    public Integer getServiceStatus() {
        return code;
    }
}

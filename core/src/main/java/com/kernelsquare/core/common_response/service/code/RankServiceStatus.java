package com.kernelsquare.core.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RankServiceStatus implements ServiceStatus {
    //error
    RANK_NOT_FOUND(2700);

    private final Integer code;

    @Override
    public Integer getServiceStatus() {
        return code;
    }
}

package com.kernel360.kernelsquare.global.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TechStackServiceStatus implements ServiceStatus {
    //error
    TECH_STACK_NOT_FOUND(2105),
    TECH_STACK_ALREADY_EXISTED(2106),

    //sccess
    TECH_STACK_CREATED(2146);

    private final Integer code;

    @Override
    public Integer getServiceStatus() {
        return code;
    }
}

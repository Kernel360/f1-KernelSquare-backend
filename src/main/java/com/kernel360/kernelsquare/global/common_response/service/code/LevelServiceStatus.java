package com.kernel360.kernelsquare.global.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum LevelServiceStatus implements ServiceStatus {
    //error
    LEVEL_NOT_FOUND(1300),
    LEVEL_ALREADY_EXISTED(1301),

    //success
    LEVEL_CREATED(1340);

    private final Integer code;

    @Override
    public Integer getServiceStatus() { return code; }

}

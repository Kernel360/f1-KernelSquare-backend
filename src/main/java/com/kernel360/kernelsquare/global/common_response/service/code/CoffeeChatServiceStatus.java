package com.kernel360.kernelsquare.global.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CoffeeChatServiceStatus implements ServiceStatus {
    COFFEE_CHAT_ROOM_CREATED(3240);

    private final Integer code;

    @Override
    public Integer getServiceStatus() {
        return code;
    }
}

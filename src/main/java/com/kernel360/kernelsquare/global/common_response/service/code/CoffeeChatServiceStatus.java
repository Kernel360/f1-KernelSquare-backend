package com.kernel360.kernelsquare.global.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CoffeeChatServiceStatus implements ServiceStatus {
    //error
    COFFEE_CHAT_ROOM_NOT_FOUND(3201),

    //success
    COFFEE_CHAT_ROOM_CREATED(3240),
    COFFEE_CHAT_ROOM_LEAVE(3242);

    private final Integer code;

    @Override
    public Integer getServiceStatus() {
        return code;
    }
}

package com.kernel360.kernelsquare.global.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CoffeeChatServiceStatus implements ServiceStatus {
    //error
    MESSAGE_TYPE_NOT_VALID(3200),
    COFFEE_CHAT_ROOM_NOT_FOUND(3201),
    COFFEE_CHAT_ROOM_NOT_ACTIVE(3202),
    MENTOR_MISMATCH(3203),
    MEMBER_MISMATCH(3204),
    AUTHORITY_NOT_VALID(3205),
    MESSAGE_DELIVERY_FAILED(3206),

    //success
    COFFEE_CHAT_ROOM_CREATED(3240),
    ROOM_ENTRY_SUCCESSFUL(3241),
    COFFEE_CHAT_ROOM_LEAVE(3242);

    private final Integer code;

    @Override
    public Integer getServiceStatus() {
        return code;
    }
}

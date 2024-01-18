package com.kernel360.kernelsquare.global.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CoffeeChatServiceStatus implements ServiceStatus {
    //error
    MESSAGE_TYPE_NOT_VALID(3200),
    COFFEE_CHAT_ROOM_NOT_FOUND(3201),
    MENTOR_MISMATCH(3202),
    MEMBER_MISMATCH(3203),
    AUTHORITY_NOT_VALID(3204),

    //success
    COFFEE_CHAT_ROOM_CREATED(3240),
    ROOM_ENTRY_SUCCESSFUL(3241);

    private final Integer code;

    @Override
    public Integer getServiceStatus() {
        return code;
    }
}

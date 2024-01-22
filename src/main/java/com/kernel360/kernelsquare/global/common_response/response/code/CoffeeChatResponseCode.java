package com.kernel360.kernelsquare.global.common_response.response.code;

import com.kernel360.kernelsquare.global.common_response.service.code.CoffeeChatServiceStatus;
import com.kernel360.kernelsquare.global.common_response.service.code.ServiceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CoffeeChatResponseCode implements ResponseCode {
    COFFEE_CHAT_ROOM_CREATED(HttpStatus.OK, CoffeeChatServiceStatus.COFFEE_CHAT_ROOM_CREATED, "채팅방 생성 성공"),
    ROOM_ENTRY_SUCCESSFUL(HttpStatus.OK, CoffeeChatServiceStatus.ROOM_ENTRY_SUCCESSFUL, "채팅방 입장 성공");

    private final HttpStatus httpStatus;
    private final ServiceStatus serviceStatus;
    private final String msg;

    @Override
    public HttpStatus getStatus() {
        return httpStatus;
    }

    @Override
    public Integer getCode() {
        return serviceStatus.getServiceStatus();
    }

    @Override
    public String getMsg() {
        return msg;
    }
}

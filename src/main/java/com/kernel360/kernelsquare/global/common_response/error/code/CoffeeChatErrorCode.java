package com.kernel360.kernelsquare.global.common_response.error.code;

import com.kernel360.kernelsquare.global.common_response.service.code.CoffeeChatServiceStatus;
import com.kernel360.kernelsquare.global.common_response.service.code.ServiceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CoffeeChatErrorCode implements ErrorCode {
    MESSAGE_TYPE_NOT_VALID(HttpStatus.BAD_REQUEST, CoffeeChatServiceStatus.MESSAGE_TYPE_NOT_VALID, "유효하지 않는 메시지 타입"),
    COFFEE_CHAT_ROOM_NOT_FOUND(HttpStatus.BAD_REQUEST, CoffeeChatServiceStatus.COFFEE_CHAT_ROOM_NOT_FOUND, "존재하지 않는 채팅방"),
    MENTOR_MISMATCH(HttpStatus.BAD_REQUEST, CoffeeChatServiceStatus.MENTOR_MISMATCH, "해당 예약창을 만든 멘토와 불일치"),
    MEMBER_MISMATCH(HttpStatus.BAD_REQUEST, CoffeeChatServiceStatus.MEMBER_MISMATCH, "해당 예약 시간의 멤버와 불일치"),
    AUTHORITY_NOT_VALID(HttpStatus.BAD_REQUEST, CoffeeChatServiceStatus.AUTHORITY_NOT_VALID, "유효하지 않는 권한");

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

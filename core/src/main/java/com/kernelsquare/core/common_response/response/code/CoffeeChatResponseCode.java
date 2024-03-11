package com.kernelsquare.core.common_response.response.code;

import org.springframework.http.HttpStatus;

import com.kernelsquare.core.common_response.service.code.CoffeeChatServiceStatus;
import com.kernelsquare.core.common_response.service.code.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CoffeeChatResponseCode implements ResponseCode {
	COFFEE_CHAT_ROOM_CREATED(HttpStatus.OK, CoffeeChatServiceStatus.COFFEE_CHAT_ROOM_CREATED, "채팅방 생성 성공"),
	ROOM_ENTRY_SUCCESSFUL(HttpStatus.OK, CoffeeChatServiceStatus.ROOM_ENTRY_SUCCESSFUL, "채팅방 입장 성공"),
	COFFEE_CHAT_ROOM_LEAVE(HttpStatus.OK, CoffeeChatServiceStatus.COFFEE_CHAT_ROOM_LEAVE, "채팅방 퇴장 성공"),
	CHAT_HISTORY_FOUND(HttpStatus.OK, CoffeeChatServiceStatus.CHAT_HISTORY_FOUND, "채팅 내역 조회 성공"),
	COFFEE_CHAT_REQUEST_FINISHED(HttpStatus.OK, CoffeeChatServiceStatus.COFFEE_CHAT_REQUEST_FINISHED, "커피챗 요청이 종료되었습니다.");

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

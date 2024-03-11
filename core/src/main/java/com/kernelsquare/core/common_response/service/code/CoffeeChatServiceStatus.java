package com.kernelsquare.core.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CoffeeChatServiceStatus implements ServiceStatus {
	//error
	MESSAGE_TYPE_NOT_VALID(3200),
	COFFEE_CHAT_ROOM_NOT_FOUND(3201),
	COFFEE_CHAT_ROOM_NOT_ACTIVE(3202),
	AUTHORITY_NOT_VALID(3205),
	MESSAGE_DELIVERY_FAILED(3206),
	COFFEE_CHAT_ROOM_EXPIRED(3207),
	COFFEE_CHAT_SELF_REQUEST_IMPOSSIBLE(3213),
	COFFEE_CHAT_REQUEST_NOT_VALID(3214),

	//success
	COFFEE_CHAT_ROOM_CREATED(3240),
	ROOM_ENTRY_SUCCESSFUL(3241),
	COFFEE_CHAT_ROOM_LEAVE(3242),
	CHAT_HISTORY_FOUND(3243),
	COFFEE_CHAT_REQUEST_FINISHED(3244);

	private final Integer code;

	@Override
	public Integer getServiceStatus() {
		return code;
	}
}

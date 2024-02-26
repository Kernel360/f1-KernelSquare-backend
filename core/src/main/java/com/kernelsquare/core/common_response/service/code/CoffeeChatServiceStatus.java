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
	COFFEE_CHAT_ROOM_CAPACITY_EXCEEDED(3208),

	//success
	COFFEE_CHAT_ROOM_CREATED(3240),
	ROOM_ENTRY_SUCCESSFUL(3241),
	COFFEE_CHAT_ROOM_LEAVE(3242),
	CHAT_HISTORY_FOUND(3243),
	CHAT_MEMBER_NOT_FOUND(3244),
	MESSAGE_COMMAND_NOT_VALID(3245),
	MESSAGE_DESTINATION_NOT_VALID(3246),
	MESSAGE_NATIVEHEADER_NOT_FOUND(3247);

	private final Integer code;

	@Override
	public Integer getServiceStatus() {
		return code;
	}
}

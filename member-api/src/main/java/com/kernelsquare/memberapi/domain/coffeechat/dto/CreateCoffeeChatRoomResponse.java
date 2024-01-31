package com.kernelsquare.memberapi.domain.coffeechat.dto;

import com.kernelsquare.domainmysql.domain.coffeechat.entity.ChatRoom;

public record CreateCoffeeChatRoomResponse(
	String roomKey
) {
	public static CreateCoffeeChatRoomResponse from(ChatRoom chatRoom) {
		return new CreateCoffeeChatRoomResponse(
			chatRoom.getRoomKey()
		);
	}
}

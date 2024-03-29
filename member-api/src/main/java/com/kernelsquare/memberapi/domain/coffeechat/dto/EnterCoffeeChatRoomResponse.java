package com.kernelsquare.memberapi.domain.coffeechat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kernelsquare.core.constants.TimeResponseFormat;
import com.kernelsquare.domainmysql.domain.coffeechat.entity.ChatRoom;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EnterCoffeeChatRoomResponse(
	String articleTitle,

	String roomKey,

	Boolean active,

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeResponseFormat.PATTERN)
	LocalDateTime expirationTime
) {
	public static EnterCoffeeChatRoomResponse of(
		String articleTitle,
		ChatRoom chatRoom
	) {
		return EnterCoffeeChatRoomResponse.builder()
			.articleTitle(articleTitle)
			.roomKey(chatRoom.getRoomKey())
			.active(chatRoom.getActive())
			.expirationTime(chatRoom.getExpirationTime())
			.build();
	}
}
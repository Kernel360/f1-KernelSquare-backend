package com.kernelsquare.memberapi.domain.coffeechat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kernelsquare.core.constants.TimeResponseFormat;
import com.kernelsquare.domainmysql.domain.coffeechat.entity.ChatRoom;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record EnterCoffeeChatRoomResponse(
	String articleTitle,

	String roomKey,

	Boolean active,

	List<ChatRoomMember> memberList,

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeResponseFormat.PATTERN)
	LocalDateTime expirationTime
) {
	public static EnterCoffeeChatRoomResponse of(
		String articleTitle,
		ChatRoom chatRoom,
		List<ChatRoomMember> chatRoomMemberList
	) {
		return EnterCoffeeChatRoomResponse.builder()
			.articleTitle(articleTitle)
			.roomKey(chatRoom.getRoomKey())
			.active(chatRoom.getActive())
			.memberList(chatRoomMemberList)
			.expirationTime(chatRoom.getExpirationTime())
			.build();
	}
}
package com.kernelsquare.memberapi.domain.coffeechat.dto;

import java.util.UUID;

import com.kernelsquare.domainmysql.domain.coffeechat.entity.ChatRoom;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CreateCoffeeChatRoomRequest(
	@NotBlank(message = "방 이름을 입력해 주세요.")
	String roomName
) {
	public static ChatRoom toEntity(CreateCoffeeChatRoomRequest createCoffeeChatRoomRequest) {
		return ChatRoom.builder()
			.roomKey(UUID.randomUUID().toString())
			.build();
	}
}

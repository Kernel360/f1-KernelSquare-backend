package com.kernel360.kernelsquare.domain.coffeechat.dto;

import com.kernel360.kernelsquare.domain.coffeechat.entity.ChatRoom;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateCoffeeChatRoomRequest(
    String roomName
) {
    public static ChatRoom toEntity(CreateCoffeeChatRoomRequest createCoffeeChatRoomRequest) {
        return ChatRoom.builder()
            .roomKey(UUID.randomUUID().toString())
            .roomName(createCoffeeChatRoomRequest.roomName())
            .build();
    }
}

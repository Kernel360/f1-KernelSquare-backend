package com.kernel360.kernelsquare.domain.coffeechat.dto;

import com.kernel360.kernelsquare.domain.coffeechat.entity.ChatRoom;
import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateRoomRequest(
    String roomName
) {
    public static ChatRoom toEntity(CreateRoomRequest createRoomRequest) {
        return ChatRoom.builder()
            .id(UUID.randomUUID().toString())
            .roomName(createRoomRequest.roomName())
            .build();
    }
}

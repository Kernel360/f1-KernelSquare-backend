package com.kernel360.kernelsquare.domain.coffeechat.dto;

import com.kernel360.kernelsquare.domain.coffeechat.entity.ChatRoom;

public record CreateCoffeeChatRoomResponse(
    String roomKey
) {
    public static CreateCoffeeChatRoomResponse from(ChatRoom chatRoom) {
        return new CreateCoffeeChatRoomResponse(
            chatRoom.getRoomKey()
        );
    }
}

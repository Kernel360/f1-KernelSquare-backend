package com.kernel360.kernelsquare.domain.coffeechat.dto;

import com.kernel360.kernelsquare.domain.coffeechat.entity.ChatRoom;
import lombok.Builder;

@Builder
public record EnterCoffeeChatRoomResponse(
    String articleTitle,
    String roomKey
) {
    public static EnterCoffeeChatRoomResponse of(String articleTitle, ChatRoom chatRoom) {
        return  EnterCoffeeChatRoomResponse.builder()
            .articleTitle(articleTitle)
            .roomKey(chatRoom.getRoomKey())
            .build();
    }
}

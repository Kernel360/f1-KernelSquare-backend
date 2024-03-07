package com.kernelsquare.domainmysql.domain.coffeechat.repository;

import com.kernelsquare.domainmysql.domain.coffeechat.entity.ChatRoom;

import java.util.List;

public interface CoffeeChatReader {
    ChatRoom findChatRoom(String roomKey);

    List<ChatRoom> findActiveChatRooms(Boolean active);
}

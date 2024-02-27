package com.kernelsquare.domainmysql.domain.coffeechat.repository;

import com.kernelsquare.domainmysql.domain.coffeechat.entity.ChatRoom;

import java.util.List;
import java.util.Optional;

public interface CoffeeChatReader {
    ChatRoom findByRoomKey(String roomKey);

    List<ChatRoom> findAllByActive(Boolean active);
}

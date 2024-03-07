package com.kernelsquare.domainmysql.domain.coffeechat.repository;

import com.kernelsquare.core.common_response.error.code.CoffeeChatErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.coffeechat.entity.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CoffeeChatReaderImpl implements CoffeeChatReader {
    private final CoffeeChatRepository coffeeChatRepository;

    @Override
    public ChatRoom findChatRoom(String roomKey) {
        return coffeeChatRepository.findByRoomKey(roomKey)
            .orElseThrow(() -> new BusinessException(CoffeeChatErrorCode.COFFEE_CHAT_ROOM_NOT_FOUND));
    }

    @Override
    public List<ChatRoom> findActiveChatRooms(Boolean active) {
        return coffeeChatRepository.findAllByActive(active);
    }
}

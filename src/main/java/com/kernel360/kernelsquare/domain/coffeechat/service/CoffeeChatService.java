package com.kernel360.kernelsquare.domain.coffeechat.service;

import com.kernel360.kernelsquare.domain.coffeechat.dto.CreateRoomRequest;
import com.kernel360.kernelsquare.domain.coffeechat.entity.ChatRoom;
import com.kernel360.kernelsquare.domain.coffeechat.repository.CoffeeChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoffeeChatService {
    private final CoffeeChatRepository coffeeChatRepository;
    public void createCoffeeChatRoom(CreateRoomRequest createRoomRequest) {
        ChatRoom chatRoom = CreateRoomRequest.toEntity(createRoomRequest);

        coffeeChatRepository.save(chatRoom);
    }
}

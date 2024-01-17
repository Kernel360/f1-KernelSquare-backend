package com.kernel360.kernelsquare.domain.coffeechat.service;

import com.kernel360.kernelsquare.domain.coffeechat.dto.CreateCoffeeChatRoomRequest;
import com.kernel360.kernelsquare.domain.coffeechat.dto.CreateCoffeeChatRoomResponse;
import com.kernel360.kernelsquare.domain.coffeechat.entity.ChatRoom;
import com.kernel360.kernelsquare.domain.coffeechat.repository.CoffeeChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoffeeChatService {
    private final CoffeeChatRepository coffeeChatRepository;
    public CreateCoffeeChatRoomResponse createCoffeeChatRoom(CreateCoffeeChatRoomRequest createCoffeeChatRoomRequest) {
        ChatRoom chatRoom = CreateCoffeeChatRoomRequest.toEntity(createCoffeeChatRoomRequest);

        ChatRoom saveChatRoom = coffeeChatRepository.save(chatRoom);

        return CreateCoffeeChatRoomResponse.from(saveChatRoom);
    }
}

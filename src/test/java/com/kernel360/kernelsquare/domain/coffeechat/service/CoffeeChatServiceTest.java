package com.kernel360.kernelsquare.domain.coffeechat.service;

import com.kernel360.kernelsquare.domain.coffeechat.dto.CreateCoffeeChatRoomRequest;
import com.kernel360.kernelsquare.domain.coffeechat.dto.CreateCoffeeChatRoomResponse;
import com.kernel360.kernelsquare.domain.coffeechat.entity.ChatRoom;
import com.kernel360.kernelsquare.domain.coffeechat.repository.CoffeeChatRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("채팅 서비스 통합 테스트")
@ExtendWith(MockitoExtension.class)
class CoffeeChatServiceTest {
    @InjectMocks
    private CoffeeChatService coffeeChatService;
    @Mock
    private CoffeeChatRepository coffeeChatRepository;

    @Test
    @DisplayName("채팅방 생성 테스트")
    void testCreateCoffeeChatRoom() {
        //given
        String roomName = "홍박사님의 명강";
        
        CreateCoffeeChatRoomRequest createCoffeeChatRoomRequest = CreateCoffeeChatRoomRequest.builder()
            .roomName(roomName)
            .build();

        ChatRoom chatRoom = CreateCoffeeChatRoomRequest.toEntity(createCoffeeChatRoomRequest);

        ChatRoom saveChatRoom = ChatRoom.builder()
            .id(1L)
            .roomKey(chatRoom.getRoomKey())
            .build();

        given(coffeeChatRepository.save(any(ChatRoom.class))).willReturn(saveChatRoom);

        //when
        CreateCoffeeChatRoomResponse response = coffeeChatService.createCoffeeChatRoom(createCoffeeChatRoomRequest);
        
        //then
        assertThat(response).isNotNull();
        assertThat(response.roomKey()).isEqualTo(saveChatRoom.getRoomKey());

        //verify
        verify(coffeeChatRepository, times(1)).save(any(ChatRoom.class));
    }
}
package com.kernel360.kernelsquare.domain.coffeechat.service;

import com.kernel360.kernelsquare.domain.coffeechat.dto.CreateCoffeeChatRoomRequest;
import com.kernel360.kernelsquare.domain.coffeechat.dto.CreateCoffeeChatRoomResponse;
import com.kernel360.kernelsquare.domain.coffeechat.dto.EnterCoffeeChatRoomRequest;
import com.kernel360.kernelsquare.domain.coffeechat.dto.EnterCoffeeChatRoomResponse;
import com.kernel360.kernelsquare.domain.coffeechat.entity.ChatRoom;
import com.kernel360.kernelsquare.domain.coffeechat.repository.CoffeeChatRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

    @Test
    @DisplayName("채팅방 생성 테스트")
    void testEnterCoffeeChatRoom() {
        //given
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            "1", null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_MENTOR")));

        given(securityContext.getAuthentication()).willReturn(authentication);

        ChatRoom chatRoom = ChatRoom.builder()
            .id(Long.valueOf(authentication.getName()))
            .roomKey("asd")
            .build();

        EnterCoffeeChatRoomRequest enterCoffeeChatRoomRequest = EnterCoffeeChatRoomRequest.builder()
            .roomId(1L)
            .memberId(Long.valueOf(authentication.getName()))
            .articleTitle("불꽃남자의 예절 주입방")
            .build();

        given(coffeeChatRepository.findById(anyLong())).willReturn(Optional.of(chatRoom));

        //when
        EnterCoffeeChatRoomResponse response = coffeeChatService.enterCoffeeChatRoom(enterCoffeeChatRoomRequest);

        //then
        assertThat(response).isNotNull();
        assertThat(response.articleTitle()).isEqualTo(chatRoom.getRoomName());
        assertThat(response.roomKey()).isEqualTo(chatRoom.getRoomKey());
        assertThat(response.active()).isTrue();

        //verify
        verify(coffeeChatRepository, times(1)).findById(anyLong());
    }
}
package com.kernel360.kernelsquare.domain.coffeechat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.kernel360.kernelsquare.domain.coffeechat.dto.CreateCoffeeChatRoomRequest;
import com.kernel360.kernelsquare.domain.coffeechat.dto.CreateCoffeeChatRoomResponse;
import com.kernel360.kernelsquare.domain.coffeechat.entity.ChatRoom;
import com.kernel360.kernelsquare.domain.coffeechat.service.CoffeeChatService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.kernel360.kernelsquare.global.common_response.response.code.CoffeeChatResponseCode.COFFEE_CHAT_ROOM_CREATED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("채팅 컨트롤러 통합 테스트")
@WithMockUser
@WebMvcTest(CoffeeChatController.class)
class CoffeeChatControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CoffeeChatService coffeeChatService;

    private ObjectMapper objectMapper = new ObjectMapper();
    
    @Test
    @DisplayName("채팅방 생성 성공시 200 OK와 메시지를 반환한다")
    void testCreateCoffeeChatRoom() throws Exception {
        //given
        String roomName = "불꽃남자의 예절 주입방";

        CreateCoffeeChatRoomRequest createCoffeeChatRoomRequest = CreateCoffeeChatRoomRequest.builder()
            .roomName(roomName)
            .build();

        ChatRoom chatRoom = CreateCoffeeChatRoomRequest.toEntity(createCoffeeChatRoomRequest);

        ChatRoom saveChatRoom = ChatRoom.builder()
            .id(1L)
            .roomKey(chatRoom.getRoomKey())
            .build();

        CreateCoffeeChatRoomResponse createCoffeeChatRoomResponse = CreateCoffeeChatRoomResponse.from(saveChatRoom);
        
        given(coffeeChatService.createCoffeeChatRoom(any(CreateCoffeeChatRoomRequest.class))).willReturn(createCoffeeChatRoomResponse);

        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        String jsonRequest = objectMapper.writeValueAsString(createCoffeeChatRoomRequest);
        
        //when & then
        mockMvc.perform(post("/api/v1/coffeechat/rooms")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8")
            .content(jsonRequest))
        .andExpect(status().is(COFFEE_CHAT_ROOM_CREATED.getStatus().value()))
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.code").value(COFFEE_CHAT_ROOM_CREATED.getCode()))
        .andExpect(jsonPath("$.msg").value(COFFEE_CHAT_ROOM_CREATED.getMsg()));
    }
}
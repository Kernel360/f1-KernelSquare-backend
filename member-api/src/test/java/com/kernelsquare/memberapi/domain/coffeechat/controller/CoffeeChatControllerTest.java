package com.kernelsquare.memberapi.domain.coffeechat.controller;

import static com.kernelsquare.core.common_response.response.code.CoffeeChatResponseCode.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.kernelsquare.memberapi.domain.coffeechat.dto.CreateCoffeeChatRoomRequest;
import com.kernelsquare.memberapi.domain.coffeechat.dto.CreateCoffeeChatRoomResponse;
import com.kernelsquare.memberapi.domain.coffeechat.dto.EnterCoffeeChatRoomRequest;
import com.kernelsquare.memberapi.domain.coffeechat.dto.EnterCoffeeChatRoomResponse;
import com.kernelsquare.memberapi.domain.coffeechat.service.CoffeeChatService;
import com.kernelsquare.domainmysql.domain.coffeechat.entity.ChatRoom;

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

		given(coffeeChatService.createCoffeeChatRoom(any(CreateCoffeeChatRoomRequest.class))).willReturn(
			createCoffeeChatRoomResponse);

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

	@Test
	@DisplayName("채팅방 입장 성공시 200 OK와 메시지를 반환한다")
	void testEnterCoffeeChatRoom() throws Exception {
		//given
		ChatRoom chatRoom = ChatRoom.builder()
			.id(1L)
			.roomKey("asd")
			.build();

		EnterCoffeeChatRoomRequest enterCoffeeChatRoomRequest = EnterCoffeeChatRoomRequest.builder()
			.roomId(1L)
			.memberId(1L)
			.articleTitle("불꽃남자의 예절 주입방")
			.build();

		EnterCoffeeChatRoomResponse enterCoffeeChatRoomResponse = EnterCoffeeChatRoomResponse.of(
			enterCoffeeChatRoomRequest.articleTitle(), chatRoom);

		given(coffeeChatService.enterCoffeeChatRoom(any(EnterCoffeeChatRoomRequest.class))).willReturn(
			enterCoffeeChatRoomResponse);

		objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		String jsonRequest = objectMapper.writeValueAsString(enterCoffeeChatRoomRequest);

		//when & then
		mockMvc.perform(post("/api/v1/coffeechat/rooms/enter")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest))
			.andExpect(status().is(ROOM_ENTRY_SUCCESSFUL.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(ROOM_ENTRY_SUCCESSFUL.getCode()))
			.andExpect(jsonPath("$.msg").value(ROOM_ENTRY_SUCCESSFUL.getMsg()));
	}
}
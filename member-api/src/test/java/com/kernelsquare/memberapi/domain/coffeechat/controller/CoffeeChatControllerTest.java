package com.kernelsquare.memberapi.domain.coffeechat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.kernelsquare.core.type.AuthorityType;
import com.kernelsquare.domainmongodb.domain.coffeechat.entity.MongoChatMessage;
import com.kernelsquare.domainmongodb.domain.coffeechat.entity.MongoMessageType;
import com.kernelsquare.domainmysql.domain.authority.entity.Authority;
import com.kernelsquare.domainmysql.domain.coffeechat.entity.ChatRoom;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member_authority.entity.MemberAuthority;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdapter;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdaptorInstance;
import com.kernelsquare.memberapi.domain.coffeechat.dto.*;
import com.kernelsquare.memberapi.domain.coffeechat.facade.CoffeeChatFacade;
import com.kernelsquare.memberapi.domain.coffeechat.service.CoffeeChatService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.stream.Stream;

import static com.kernelsquare.core.common_response.response.code.CoffeeChatResponseCode.*;
import static com.kernelsquare.memberapi.config.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("채팅 컨트롤러 통합 테스트")
@WithMockUser
@WebMvcTest(CoffeeChatController.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
class CoffeeChatControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private CoffeeChatService coffeeChatService;
	@MockBean
	private CoffeeChatFacade coffeeChatFacade;

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
			.reservationId(1L)
			.articleTitle("불꽃남자의 예절 주입방")
			.build();

		Level level = Level.builder()
			.name(6L)
			.imageUrl("1.jpg")
			.build();

		Member member = Member.builder()
			.id(1L)
			.nickname("machine")
			.email("awdag@nsavasc.om")
			.password("hashed")
			.experience(1200L)
			.introduction("basfas")
			.authorities(List.of(
				MemberAuthority.builder()
					.member(Member.builder().build())
					.authority(Authority.builder().authorityType(AuthorityType.ROLE_USER).build())
					.build()))
			.imageUrl("agawsc")
			.level(level)
			.build();

		MemberAdapter memberAdapter = new MemberAdapter(MemberAdaptorInstance.of(member));

		ChatRoomMember chatRoomMember = ChatRoomMember.from(member);

		EnterCoffeeChatRoomResponse enterCoffeeChatRoomResponse = EnterCoffeeChatRoomResponse.of(
			enterCoffeeChatRoomRequest.articleTitle(), chatRoom, List.of(chatRoomMember));

		given(coffeeChatService.enterCoffeeChatRoom(any(EnterCoffeeChatRoomRequest.class), any(MemberAdapter.class))).willReturn(
			enterCoffeeChatRoomResponse);

		objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		String jsonRequest = objectMapper.writeValueAsString(enterCoffeeChatRoomRequest);

		//when & then
		mockMvc.perform(post("/api/v1/coffeechat/rooms/enter")
				.with(csrf())
				.with(user(memberAdapter))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest))
			.andExpect(status().is(ROOM_ENTRY_SUCCESSFUL.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(ROOM_ENTRY_SUCCESSFUL.getCode()))
			.andExpect(jsonPath("$.msg").value(ROOM_ENTRY_SUCCESSFUL.getMsg()));
	}

	@Test
	@DisplayName("채팅 내역 조회 성공시 200 OK와 메시지를 반환한다.")
	void testFindChatHistory() throws Exception {
		//given
		MongoChatMessage mongoChatMessage = MongoChatMessage.builder()
			.roomKey("key")
			.type(MongoMessageType.TALK)
			.message("hi")
			.sender("에키드나")
			.build();

		List<FindMongoChatMessage> chatHistory = Stream.of(mongoChatMessage)
			.map(FindMongoChatMessage::from)
			.toList();

		FindChatHistoryResponse findChatHistoryResponse = FindChatHistoryResponse.of(chatHistory);

		given(coffeeChatService.findChatHistory(mongoChatMessage.getRoomKey())).willReturn(findChatHistoryResponse);

		//when & then
		mockMvc.perform(get("/api/v1/coffeechat/rooms/" + mongoChatMessage.getRoomKey())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"))
			.andExpect(status().is(CHAT_HISTORY_FOUND.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(CHAT_HISTORY_FOUND.getCode()))
			.andExpect(jsonPath("$.msg").value(CHAT_HISTORY_FOUND.getMsg()));
	}

	@Test
	@DisplayName("커피챗 요청 성공시 200 OK와 메시지를 반환한다.")
	void testRequestCoffeeChat() throws Exception {
		//given
		Member member = Member.builder()
			.id(1L)
			.nickname("machine")
			.email("awdag@nsavasc.om")
			.password("hashed")
			.experience(1200L)
			.introduction("basfas")
			.authorities(List.of(
				MemberAuthority.builder()
					.member(Member.builder().build())
					.authority(Authority.builder().authorityType(AuthorityType.ROLE_USER).build())
					.build()))
			.imageUrl("agawsc")
			.build();

		MemberAdapter memberAdapter = new MemberAdapter(MemberAdaptorInstance.of(member));

		doNothing()
			.when(coffeeChatFacade)
			.sendCoffeeChatRequest(any(MemberAdapter.class), anyLong());

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/api/v1/coffeechat/request/1")
				.with(csrf())
				.with(user(memberAdapter))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"));

		//then
		resultActions
			.andExpect(status().is(COFFEE_CHAT_REQUEST_FINISHED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("coffeechat-request",
				getDocumentResponse(),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지")
				)));

		//verify
		verify(coffeeChatFacade, times(1)).sendCoffeeChatRequest(any(MemberAdapter.class), anyLong());
	}
}
package com.kernelsquare.memberapi.domain.coffeechat.controller;

import static com.kernelsquare.core.common_response.response.code.CoffeeChatResponseCode.*;
import static com.kernelsquare.memberapi.config.ApiDocumentUtils.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.kernelsquare.core.type.AuthorityType;
import com.kernelsquare.domainmongodb.domain.coffeechat.entity.MongoChatMessage;
import com.kernelsquare.domainmongodb.domain.coffeechat.entity.MongoMessageType;
import com.kernelsquare.domainmysql.domain.authority.entity.Authority;
import com.kernelsquare.domainmysql.domain.coffeechat.entity.ChatRoom;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member_authority.entity.MemberAuthority;
import com.kernelsquare.memberapi.config.RestDocsControllerTest;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdapter;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdaptorInstance;
import com.kernelsquare.memberapi.domain.coffeechat.dto.ChatRoomMember;
import com.kernelsquare.memberapi.domain.coffeechat.dto.CreateCoffeeChatRoomRequest;
import com.kernelsquare.memberapi.domain.coffeechat.dto.CreateCoffeeChatRoomResponse;
import com.kernelsquare.memberapi.domain.coffeechat.dto.EnterCoffeeChatRoomRequest;
import com.kernelsquare.memberapi.domain.coffeechat.dto.EnterCoffeeChatRoomResponse;
import com.kernelsquare.memberapi.domain.coffeechat.dto.FindChatHistoryResponse;
import com.kernelsquare.memberapi.domain.coffeechat.dto.FindMongoChatMessage;
import com.kernelsquare.memberapi.domain.coffeechat.facade.CoffeeChatFacade;
import com.kernelsquare.memberapi.domain.coffeechat.service.CoffeeChatService;

@DisplayName("채팅 컨트롤러 테스트")
@WithMockUser
@WebMvcTest(CoffeeChatController.class)
class CoffeeChatControllerTest extends RestDocsControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private CoffeeChatService coffeeChatService;
	@MockBean
	private CoffeeChatFacade coffeeChatFacade;

	private ObjectMapper objectMapper = new ObjectMapper().setPropertyNamingStrategy(
		PropertyNamingStrategies.SNAKE_CASE);

	@Test
	@DisplayName("채팅방 생성 성공시 200 OK와 메시지를 반환한다")
	void testCreateCoffeeChatRoom() throws Exception {
		//given
		String roomName = "불꽃남자의 예절 주입방";

		CreateCoffeeChatRoomRequest createCoffeeChatRoomRequest = CreateCoffeeChatRoomRequest.builder()
			.roomName(roomName)
			.build();

		ChatRoom chatRoom = CreateCoffeeChatRoomRequest.toEntity(createCoffeeChatRoomRequest);

		ChatRoom saveChatRoom = ChatRoom.builder().id(1L).roomKey(chatRoom.getRoomKey()).build();

		CreateCoffeeChatRoomResponse createCoffeeChatRoomResponse = CreateCoffeeChatRoomResponse.from(saveChatRoom);

		given(coffeeChatService.createCoffeeChatRoom(any(CreateCoffeeChatRoomRequest.class))).willReturn(
			createCoffeeChatRoomResponse);

		String jsonRequest = objectMapper.writeValueAsString(createCoffeeChatRoomRequest);

		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/coffeechat/rooms")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8")
			.content(jsonRequest));

		//then
		resultActions.andExpect(status().is(COFFEE_CHAT_ROOM_CREATED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("coffee-chat-room-created", getDocumentRequest(), getDocumentResponse(),
				requestFields(fieldWithPath("room_name").type(JsonFieldType.STRING).description("채팅방 이름")),
				responseFields(
					fieldWithPath("data").type(JsonFieldType.OBJECT).description("데이터"),
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("커스텀 응답 코드"),
					fieldWithPath("msg").type(JsonFieldType.STRING).description("메시지"),
					fieldWithPath("data.room_key").type(JsonFieldType.STRING).description("채팅방 키")
				)));

		//verify
		verify(coffeeChatService, times(1)).createCoffeeChatRoom(any(CreateCoffeeChatRoomRequest.class));
	}

	@Test
	@DisplayName("채팅방 입장 성공시 200 OK와 메시지를 반환한다")
	void testEnterCoffeeChatRoom() throws Exception {
		//given
		ChatRoom chatRoom = ChatRoom.builder().id(1L).roomKey("asd").expirationTime(LocalDateTime.now()).build();

		EnterCoffeeChatRoomRequest enterCoffeeChatRoomRequest = EnterCoffeeChatRoomRequest.builder()
			.reservationId(1L)
			.articleTitle("불꽃남자의 예절 주입방")
			.build();

		Level level = Level.builder().name(6L).imageUrl("1.jpg").build();

		Member member = Member.builder()
			.id(1L)
			.nickname("machine")
			.email("awdag@nsavasc.om")
			.password("hashed")
			.experience(1200L)
			.introduction("basfas")
			.authorities(List.of(MemberAuthority.builder()
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

		given(coffeeChatService.enterCoffeeChatRoom(any(EnterCoffeeChatRoomRequest.class),
			any(MemberAdapter.class))).willReturn(enterCoffeeChatRoomResponse);

		String jsonRequest = objectMapper.writeValueAsString(enterCoffeeChatRoomRequest);

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/api/v1/coffeechat/rooms/enter").with(csrf())
				.with(user(memberAdapter))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest));

		//then
		resultActions
			.andExpect(status().is(ROOM_ENTRY_SUCCESSFUL.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("room-entry-success", getDocumentRequest(), getDocumentResponse(),
				requestFields(fieldWithPath("reservation_id").type(JsonFieldType.NUMBER).description("예약 아이디"),
					fieldWithPath("article_title").type(JsonFieldType.STRING).description("게시글 제목")
				),
				responseFields(
					fieldWithPath("data").type(JsonFieldType.OBJECT).description("데이터"),
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("커스텀 응답 코드"),
					fieldWithPath("msg").type(JsonFieldType.STRING).description("메시지"),
					fieldWithPath("data.article_title").type(JsonFieldType.STRING).description("게시글 제목"),
					fieldWithPath("data.room_key").type(JsonFieldType.STRING).description("채팅방 키"),
					fieldWithPath("data.active").type(JsonFieldType.BOOLEAN).description("활성화 여부"),
					fieldWithPath("data.member_list").type(JsonFieldType.ARRAY).description("채팅방 멤버 리스트"),
					fieldWithPath("data.member_list[].member_id").type(JsonFieldType.NUMBER).description("멤버 아이디"),
					fieldWithPath("data.member_list[].nickname").type(JsonFieldType.STRING).description("멤버 닉네임"),
					fieldWithPath("data.member_list[].member_image_url").type(JsonFieldType.STRING)
						.description("멤버 이미지 URL"),
					fieldWithPath("data.expiration_time").type(JsonFieldType.STRING).description("채팅방 만료 시간")
				)));

		//verify
		verify(coffeeChatService, times(1)).enterCoffeeChatRoom(any(EnterCoffeeChatRoomRequest.class),
			any(MemberAdapter.class));
		verify(coffeeChatService, only()).enterCoffeeChatRoom(any(EnterCoffeeChatRoomRequest.class),
			any(MemberAdapter.class));
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
			.sendTime(LocalDateTime.now())
			.build();

		List<FindMongoChatMessage> chatHistory = Stream.of(mongoChatMessage).map(FindMongoChatMessage::from).toList();

		FindChatHistoryResponse findChatHistoryResponse = FindChatHistoryResponse.of(chatHistory);

		given(coffeeChatService.findChatHistory(mongoChatMessage.getRoomKey())).willReturn(findChatHistoryResponse);

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.get("/api/v1/coffeechat/rooms/" + mongoChatMessage.getRoomKey())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"));

		//then
		resultActions
			.andExpect(status().is(CHAT_HISTORY_FOUND.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("chat-history-found", getDocumentResponse(),
				responseFields(
					fieldWithPath("data").type(JsonFieldType.OBJECT).description("데이터"),
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("커스텀 응답 코드"),
					fieldWithPath("msg").type(JsonFieldType.STRING).description("메시지"),
					fieldWithPath("data.chat_history").type(JsonFieldType.ARRAY).description("채팅 내역"),
					fieldWithPath("data.chat_history[].room_key").type(JsonFieldType.STRING).description("채팅방 키"),
					fieldWithPath("data.chat_history[].type").type(JsonFieldType.STRING).description("채팅 타입"),
					fieldWithPath("data.chat_history[].message").type(JsonFieldType.STRING).description("채팅 메시지"),
					fieldWithPath("data.chat_history[].sender").type(JsonFieldType.STRING).description("채팅 보낸 사람"),
					fieldWithPath("data.chat_history[].send_time").type(JsonFieldType.STRING).description("채팅 보낸 시간")
				)));

		//verify
		verify(coffeeChatService, times(1)).findChatHistory(mongoChatMessage.getRoomKey());
		verify(coffeeChatService, only()).findChatHistory(mongoChatMessage.getRoomKey());
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
			.authorities(List.of(MemberAuthority.builder()
				.member(Member.builder().build())
				.authority(Authority.builder().authorityType(AuthorityType.ROLE_USER).build())
				.build()))
			.imageUrl("agawsc")
			.build();

		MemberAdapter memberAdapter = new MemberAdapter(MemberAdaptorInstance.of(member));

		doNothing().when(coffeeChatFacade).sendCoffeeChatRequest(any(MemberAdapter.class), anyLong());

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/api/v1/coffeechat/request/1")
				.with(csrf())
				.with(user(memberAdapter))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"));

		//then
		resultActions.andExpect(status().is(COFFEE_CHAT_REQUEST_FINISHED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("coffee-chat-request-finished", getDocumentResponse(),
				responseFields(fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"))));

		//verify
		verify(coffeeChatFacade, times(1)).sendCoffeeChatRequest(any(MemberAdapter.class), anyLong());
	}

	@Test
	@DisplayName("채팅방 퇴장 성공시 200 OK와 메시지를 반환한다.")
	void testLeaveCoffeeChatRoom() throws Exception {
		//given
		String roomKey = "key";

		doNothing().when(coffeeChatService).leaveCoffeeChatRoom(roomKey);

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.post("/api/v1/coffeechat/rooms/" + roomKey)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"));

		//then
		resultActions.andExpect(status().is(COFFEE_CHAT_ROOM_LEAVE.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("coffee-chat-room-leave", getDocumentResponse(),
				responseFields(fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"))));

		//verify
		verify(coffeeChatService, times(1)).leaveCoffeeChatRoom(roomKey);
	}
}
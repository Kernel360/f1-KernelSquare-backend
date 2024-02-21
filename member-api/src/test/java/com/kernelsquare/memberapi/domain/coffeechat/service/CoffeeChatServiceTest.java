package com.kernelsquare.memberapi.domain.coffeechat.service;

import com.kernelsquare.core.type.AuthorityType;
import com.kernelsquare.domainmongodb.domain.coffeechat.entity.MongoChatMessage;
import com.kernelsquare.domainmongodb.domain.coffeechat.entity.MongoMessageType;
import com.kernelsquare.domainmongodb.domain.coffeechat.repository.MongoChatMessageRepository;
import com.kernelsquare.domainmysql.domain.authority.entity.Authority;
import com.kernelsquare.domainmysql.domain.coffeechat.entity.ChatRoom;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member_authority.entity.MemberAuthority;
import com.kernelsquare.domainmysql.domain.reservation.entity.Reservation;
import com.kernelsquare.domainmysql.domain.reservation.repository.ReservationRepository;
import com.kernelsquare.domainmysql.domain.reservation_article.entity.ReservationArticle;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdapter;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdaptorInstance;
import com.kernelsquare.memberapi.domain.coffeechat.dto.EnterCoffeeChatRoomRequest;
import com.kernelsquare.memberapi.domain.coffeechat.dto.EnterCoffeeChatRoomResponse;
import com.kernelsquare.memberapi.domain.coffeechat.dto.FindChatHistoryResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

@DisplayName("채팅 서비스 통합 테스트")
@ExtendWith(MockitoExtension.class)
class CoffeeChatServiceTest {
	@InjectMocks
	private CoffeeChatService coffeeChatService;
	@Mock
	private ReservationRepository reservationRepository;
	@Mock
	private MongoChatMessageRepository mongoChatMessageRepository;

	@Test
	@DisplayName("채팅방 입장 테스트")
	void testEnterCoffeeChatRoom() {
		//given
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		SecurityContextHolder.setContext(securityContext);

		ChatRoom chatRoom = ChatRoom.builder()
			.id(1L)
			.roomKey("asd")
			.expirationTime(LocalDateTime.now().plusMinutes(30))
			.build();

		EnterCoffeeChatRoomRequest enterCoffeeChatRoomRequest = EnterCoffeeChatRoomRequest.builder()
			.reservationId(1L)
			.articleTitle("불꽃남자의 예절 주입방")
			.build();

		Level level = Level.builder()
			.name(6L)
			.imageUrl("1.jpg")
			.build();

		Member mentor = Member.builder()
			.id(1L)
			.nickname("machine")
			.email("awdag@nsavasc.om")
			.password("hashed")
			.experience(1200L)
			.introduction("basfas")
			.authorities(List.of(
				MemberAuthority.builder()
					.member(Member.builder().build())
					.authority(Authority.builder().authorityType(AuthorityType.ROLE_MENTOR).build())
					.build()))
			.imageUrl("meto")
			.level(level)
			.build();

		Member mentee = Member.builder()
			.id(2L)
			.nickname("mmmmm")
			.email("ttttt@nsavasc.om")
			.password("easd")
			.experience(10L)
			.introduction("gas")
			.authorities(List.of(
				MemberAuthority.builder()
					.member(Member.builder().build())
					.authority(Authority.builder().authorityType(AuthorityType.ROLE_USER).build())
					.build()))
			.imageUrl("mete")
			.level(level)
			.build();

		LocalDateTime startTime = LocalDateTime.now().minusMinutes(1L);
		LocalDateTime endTime = startTime.plusMinutes(30L);

		ReservationArticle reservationArticle = ReservationArticle.builder()
			.id(1L)
			.content("abc")
			.startTime(startTime)
			.member(mentor)
			.title("ratest")
			.build();

		Reservation reservation = Reservation.builder()
			.chatRoom(chatRoom)
			.startTime(startTime)
			.endTime(endTime)
			.reservationArticle(reservationArticle)
			.build();

		reservation.addMember(mentee);

		MemberAdapter memberAdapter = new MemberAdapter(MemberAdaptorInstance.of(mentor));

		given(reservationRepository.findById(anyLong())).willReturn(Optional.of(reservation));

		//when
		EnterCoffeeChatRoomResponse response = coffeeChatService.enterCoffeeChatRoom(enterCoffeeChatRoomRequest, memberAdapter);

		//then
		assertThat(response).isNotNull();
		assertThat(response.articleTitle()).isEqualTo(chatRoom.getRoomName());
		assertThat(response.roomKey()).isEqualTo(chatRoom.getRoomKey());
		assertThat(response.active()).isTrue();

		//verify
		verify(reservationRepository, times(1)).findById(anyLong());
	}

	@Test
	@DisplayName("채팅 내역 조회 테스트")
	void testFindChatHistory() {
		//given
		MongoChatMessage mongoChatMessage = MongoChatMessage.builder()
			.roomKey("key")
			.type(MongoMessageType.TALK)
			.message("hi")
			.sender("에키드나")
			.sendTime(LocalDateTime.now())
			.build();

		given(mongoChatMessageRepository.findAllByRoomKey(anyString())).willReturn(List.of(mongoChatMessage));

		//when
		FindChatHistoryResponse findChatHistoryResponse = coffeeChatService.findChatHistory(mongoChatMessage.getRoomKey());

		//then
		assertThat(findChatHistoryResponse.chatHistory()).isNotNull();
		assertThat(findChatHistoryResponse.chatHistory().get(0).message()).isEqualTo(mongoChatMessage.getMessage());
		assertThat(findChatHistoryResponse.chatHistory().get(0).sender()).isEqualTo(mongoChatMessage.getSender());
		assertThat(findChatHistoryResponse.chatHistory().get(0).roomKey()).isEqualTo(mongoChatMessage.getRoomKey());
		assertThat(findChatHistoryResponse.chatHistory().get(0).type()).isEqualTo(mongoChatMessage.getType());
		assertThat(findChatHistoryResponse.chatHistory().get(0).sendTime()).isEqualTo(mongoChatMessage.getSendTime());

		//verify
		verify(mongoChatMessageRepository, times(1)).findAllByRoomKey(anyString());
	}
}
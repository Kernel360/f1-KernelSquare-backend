package com.kernelsquare.memberapi.domain.coffeechat.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kernelsquare.core.common_response.error.code.CoffeeChatErrorCode;
import com.kernelsquare.core.common_response.error.code.ReservationErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.core.type.MessageType;
import com.kernelsquare.domainmongodb.domain.coffeechat.repository.MongoChatMessageRepository;
import com.kernelsquare.domainmysql.domain.coffeechat.entity.ChatRoom;
import com.kernelsquare.domainmysql.domain.coffeechat.repository.CoffeeChatRepository;
import com.kernelsquare.domainmysql.domain.reservation.entity.Reservation;
import com.kernelsquare.domainmysql.domain.reservation.repository.ReservationRepository;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdapter;
import com.kernelsquare.memberapi.domain.coffeechat.dto.ChatMessageResponse;
import com.kernelsquare.memberapi.domain.coffeechat.dto.ChatRoomMember;
import com.kernelsquare.memberapi.domain.coffeechat.dto.CreateCoffeeChatRoomRequest;
import com.kernelsquare.memberapi.domain.coffeechat.dto.CreateCoffeeChatRoomResponse;
import com.kernelsquare.memberapi.domain.coffeechat.dto.EnterCoffeeChatRoomRequest;
import com.kernelsquare.memberapi.domain.coffeechat.dto.EnterCoffeeChatRoomResponse;
import com.kernelsquare.memberapi.domain.coffeechat.dto.FindChatHistoryResponse;
import com.kernelsquare.memberapi.domain.coffeechat.dto.FindMongoChatMessage;
import com.kernelsquare.memberapi.domain.coffeechat.validation.CoffeeChatValidation;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CoffeeChatService {
	private final CoffeeChatRepository coffeeChatRepository;
	private final SimpMessageSendingOperations sendingOperations;
	private final MongoChatMessageRepository mongoChatMessageRepository;
	private final ReservationRepository reservationRepository;

	private final ConcurrentHashMap<String, List<ChatRoomMember>> chatRoomMemberList = new ConcurrentHashMap<>();

	@Transactional
	public CreateCoffeeChatRoomResponse createCoffeeChatRoom(CreateCoffeeChatRoomRequest createCoffeeChatRoomRequest) {
		ChatRoom chatRoom = CreateCoffeeChatRoomRequest.toEntity(createCoffeeChatRoomRequest);

		ChatRoom saveChatRoom = coffeeChatRepository.save(chatRoom);

		return CreateCoffeeChatRoomResponse.from(saveChatRoom);
	}

	@Transactional
	public EnterCoffeeChatRoomResponse enterCoffeeChatRoom(EnterCoffeeChatRoomRequest enterCoffeeChatRoomRequest,
		MemberAdapter memberAdapter) {

		Reservation reservation = reservationRepository.findById(enterCoffeeChatRoomRequest.reservationId())
			.orElseThrow(() -> new BusinessException(ReservationErrorCode.RESERVATION_NOT_FOUND));

		ChatRoom chatRoom = reservation.getChatRoom();

		CoffeeChatValidation.validateChatRoom(chatRoom);

		switch (CoffeeChatValidation.validatePermission(reservation, memberAdapter)) {
			case MENTOR -> {
				return mentorEnter(enterCoffeeChatRoomRequest, chatRoom, memberAdapter);
			}
			case MENTEE -> {
				return menteeEnter(enterCoffeeChatRoomRequest, chatRoom, memberAdapter);
			}
			default -> throw new BusinessException(CoffeeChatErrorCode.AUTHORITY_NOT_VALID);
		}
	}

	public EnterCoffeeChatRoomResponse mentorEnter(EnterCoffeeChatRoomRequest enterCoffeeChatRoomRequest,
		ChatRoom chatRoom, MemberAdapter memberAdapter) {

		chatRoom.activateRoom(enterCoffeeChatRoomRequest.articleTitle());

		//TODO 중복 입장에 대한 정책이 정해지면 로직 구현
		chatRoomMemberList.computeIfAbsent(chatRoom.getRoomKey(), k -> new ArrayList<>())
			.add(ChatRoomMember.from(memberAdapter.getMember()));

		return EnterCoffeeChatRoomResponse.of(enterCoffeeChatRoomRequest.articleTitle(), chatRoom,
			chatRoomMemberList.get(chatRoom.getRoomKey()));
	}

	public EnterCoffeeChatRoomResponse menteeEnter(EnterCoffeeChatRoomRequest enterCoffeeChatRoomRequest,
		ChatRoom chatRoom, MemberAdapter memberAdapter) {
		CoffeeChatValidation.validateChatRoomActive(chatRoom);

		//TODO 중복 입장에 대한 정책이 정해지면 로직 구현
		chatRoomMemberList.get(chatRoom.getRoomKey()).add(ChatRoomMember.from(memberAdapter.getMember()));

		return EnterCoffeeChatRoomResponse.of(enterCoffeeChatRoomRequest.articleTitle(), chatRoom,
			chatRoomMemberList.get(chatRoom.getRoomKey()));
	}

	@Transactional
	public void leaveCoffeeChatRoom(String roomKey) {
		ChatRoom chatRoom = coffeeChatRepository.findByRoomKey(roomKey)
			.orElseThrow(() -> new BusinessException(CoffeeChatErrorCode.COFFEE_CHAT_ROOM_NOT_FOUND));

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		//TODO 특정 채팅방의 유저 리스트가 필요하다면?
	}

	public FindChatHistoryResponse findChatHistory(String roomKey) {
		List<FindMongoChatMessage> chatHistory = mongoChatMessageRepository.findAllByRoomKey(roomKey).stream()
			.map(FindMongoChatMessage::from)
			.toList();

		return FindChatHistoryResponse.of(chatHistory);
	}

	@Transactional
	@Scheduled(cron = "0 0/30 * * * *")
	public void disableRoom() {
		List<ChatRoom> chatRooms = coffeeChatRepository.findAllByActive(true);
		chatRooms.forEach(chatRoom -> {
			chatRoom.deactivateRoom();

			ChatMessageResponse message = ChatMessageResponse.builder()
				.type(MessageType.EXPIRE)
				.roomKey(chatRoom.getRoomKey())
				.sender("system")
				.message("채팅방 사용 시간이 만료되었습니다.")
				.sendTime(LocalDateTime.now())
				.build();

			sendingOperations.convertAndSend("/topic/chat/room/" + message.getRoomKey(), message);
		});
	}
}

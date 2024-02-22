package com.kernelsquare.memberapi.domain.coffeechat.service;

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
import com.kernelsquare.memberapi.domain.coffeechat.component.ChatRoomMemberManager;
import com.kernelsquare.memberapi.domain.coffeechat.dto.*;
import com.kernelsquare.memberapi.domain.coffeechat.validation.CoffeeChatValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CoffeeChatService {
	private final CoffeeChatRepository coffeeChatRepository;
	private final SimpMessageSendingOperations sendingOperations;
	private final MongoChatMessageRepository mongoChatMessageRepository;
	private final ReservationRepository reservationRepository;
	private final ChatRoomMemberManager chatRoomMemberManager;

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

		chatRoomMemberManager.addChatRoom(chatRoom.getRoomKey());

		CoffeeChatValidation.validateDuplicateEntry(chatRoomMemberManager, sendingOperations, chatRoom, memberAdapter);

		return EnterCoffeeChatRoomResponse.of(enterCoffeeChatRoomRequest.articleTitle(), chatRoom,
            chatRoomMemberManager.getChatRoom(chatRoom.getRoomKey()));
	}

	public EnterCoffeeChatRoomResponse menteeEnter(EnterCoffeeChatRoomRequest enterCoffeeChatRoomRequest,
		                                            ChatRoom chatRoom, MemberAdapter memberAdapter) {

		CoffeeChatValidation.validateChatRoomActive(chatRoom);

		CoffeeChatValidation.validateDuplicateEntry(chatRoomMemberManager, sendingOperations, chatRoom, memberAdapter);

		return EnterCoffeeChatRoomResponse.of(enterCoffeeChatRoomRequest.articleTitle(), chatRoom,
                                                chatRoomMemberManager.getChatRoom(chatRoom.getRoomKey()));
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

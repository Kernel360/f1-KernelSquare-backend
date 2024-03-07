package com.kernelsquare.memberapi.domain.coffeechat.service;

import com.kernelsquare.core.common_response.error.code.CoffeeChatErrorCode;
import com.kernelsquare.core.common_response.error.code.ReservationErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmongodb.domain.coffeechat.repository.MongoChatMessageRepository;
import com.kernelsquare.domainmysql.domain.coffeechat.entity.ChatRoom;
import com.kernelsquare.domainmysql.domain.reservation.entity.Reservation;
import com.kernelsquare.domainmysql.domain.reservation.repository.ReservationRepository;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdapter;
import com.kernelsquare.memberapi.domain.coffeechat.component.ChatRoomMemberManager;
import com.kernelsquare.memberapi.domain.coffeechat.dto.EnterCoffeeChatRoomRequest;
import com.kernelsquare.memberapi.domain.coffeechat.dto.EnterCoffeeChatRoomResponse;
import com.kernelsquare.memberapi.domain.coffeechat.dto.FindChatHistoryResponse;
import com.kernelsquare.memberapi.domain.coffeechat.dto.FindMongoChatMessage;
import com.kernelsquare.memberapi.domain.coffeechat.validation.CoffeeChatValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoffeeChatService {
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

		CoffeeChatValidation.validateChatRoomCapacity(chatRoomMemberManager, chatRoom);

		CoffeeChatValidation.validateDuplicateEntry(chatRoomMemberManager, sendingOperations, chatRoom, memberAdapter);

		return EnterCoffeeChatRoomResponse.of(enterCoffeeChatRoomRequest.articleTitle(), chatRoom,
            chatRoomMemberManager.getChatRoom(chatRoom.getRoomKey()));
	}

	public EnterCoffeeChatRoomResponse menteeEnter(EnterCoffeeChatRoomRequest enterCoffeeChatRoomRequest,
		                                            ChatRoom chatRoom, MemberAdapter memberAdapter) {

		CoffeeChatValidation.validateChatRoomActive(chatRoom);

		CoffeeChatValidation.validateChatRoomCapacity(chatRoomMemberManager, chatRoom);

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
}

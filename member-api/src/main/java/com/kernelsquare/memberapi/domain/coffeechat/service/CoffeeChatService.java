package com.kernelsquare.memberapi.domain.coffeechat.service;

import com.kernelsquare.core.common_response.error.code.CoffeeChatErrorCode;
import com.kernelsquare.core.common_response.error.code.ReservationErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmongodb.domain.coffeechat.repository.MongoChatMessageRepository;
import com.kernelsquare.domainmysql.domain.coffeechat.command.CoffeeChatCommand;
import com.kernelsquare.domainmysql.domain.coffeechat.entity.ChatRoom;
import com.kernelsquare.domainmysql.domain.coffeechat.info.CoffeeChatInfo;
import com.kernelsquare.domainmysql.domain.coffeechat.repository.CoffeeChatRepository;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberReader;
import com.kernelsquare.domainmysql.domain.reservation.entity.Reservation;
import com.kernelsquare.domainmysql.domain.reservation.repository.ReservationRepository;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdapter;
import com.kernelsquare.memberapi.domain.coffeechat.dto.*;
import com.kernelsquare.memberapi.domain.coffeechat.validation.CoffeeChatValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class CoffeeChatService {
	private final CoffeeChatRepository coffeeChatRepository;
	private final MongoChatMessageRepository mongoChatMessageRepository;
	private final ReservationRepository reservationRepository;
	private final MemberReader memberReader;

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

	@Transactional(readOnly = true)
	public CoffeeChatInfo coffeeChatRequest(CoffeeChatCommand.RequestCommand command) {
		Member recipient = memberReader.findMember(command.recipientId());
		Member sender = command.sender();

		CoffeeChatValidation.validateCoffeeChatRequest(sender, recipient);

		return CoffeeChatInfo.of(sender,recipient);
	}
}

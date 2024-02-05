package com.kernelsquare.memberapi.domain.coffeechat.service;

import java.time.LocalDateTime;
import java.util.List;

import com.kernelsquare.domainmongodb.domain.coffeechat.repository.MongoChatMessageRepository;
import com.kernelsquare.memberapi.domain.coffeechat.dto.*;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kernelsquare.core.common_response.error.code.CoffeeChatErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.core.type.MessageType;
import com.kernelsquare.domainmysql.domain.coffeechat.entity.ChatRoom;
import com.kernelsquare.domainmysql.domain.coffeechat.repository.CoffeeChatRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CoffeeChatService {
	private final CoffeeChatRepository coffeeChatRepository;
	private final SimpMessageSendingOperations sendingOperations;
	private final MongoChatMessageRepository mongoChatMessageRepository;

	@Transactional
	public CreateCoffeeChatRoomResponse createCoffeeChatRoom(CreateCoffeeChatRoomRequest createCoffeeChatRoomRequest) {
		ChatRoom chatRoom = CreateCoffeeChatRoomRequest.toEntity(createCoffeeChatRoomRequest);

		ChatRoom saveChatRoom = coffeeChatRepository.save(chatRoom);

		return CreateCoffeeChatRoomResponse.from(saveChatRoom);
	}

	@Transactional
	public EnterCoffeeChatRoomResponse enterCoffeeChatRoom(EnterCoffeeChatRoomRequest enterCoffeeChatRoomRequest) {
		ChatRoom chatRoom = coffeeChatRepository.findById(enterCoffeeChatRoomRequest.roomId())
			.orElseThrow(() -> new BusinessException(CoffeeChatErrorCode.COFFEE_CHAT_ROOM_NOT_FOUND));

		if (!chatRoom.getExpirationTime().isAfter(LocalDateTime.now())) {
			throw new BusinessException(CoffeeChatErrorCode.COFFEE_CHAT_ROOM_EXPIRED);
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_MENTOR"))) {
			if (Long.valueOf(authentication.getName()).equals(enterCoffeeChatRoomRequest.memberId())) {

				chatRoom.activateRoom(enterCoffeeChatRoomRequest.articleTitle());

				return EnterCoffeeChatRoomResponse.of(enterCoffeeChatRoomRequest.articleTitle(), chatRoom);
			} else {
				throw new BusinessException(CoffeeChatErrorCode.MENTOR_MISMATCH);
			}
		} else if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"))) {
			if (Boolean.FALSE.equals(chatRoom.getActive())) {
				throw new BusinessException(CoffeeChatErrorCode.COFFEE_CHAT_ROOM_NOT_ACTIVE);

			} else if (Long.valueOf(authentication.getName()).equals(enterCoffeeChatRoomRequest.memberId())) {
				return EnterCoffeeChatRoomResponse.of(enterCoffeeChatRoomRequest.articleTitle(), chatRoom);

			} else {
				throw new BusinessException(CoffeeChatErrorCode.MEMBER_MISMATCH);
			}

		} else {
			throw new BusinessException(CoffeeChatErrorCode.AUTHORITY_NOT_VALID);
		}
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

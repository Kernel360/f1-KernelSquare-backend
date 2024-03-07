package com.kernelsquare.memberapi.domain.coffeechat.controller;

import com.kernelsquare.core.common_response.error.code.CoffeeChatErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.memberapi.domain.coffeechat.component.ChatRoomMemberManager;
import com.kernelsquare.memberapi.domain.coffeechat.dto.ChatMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {
	private final KafkaTemplate<String, Object> kafkaTemplate;
	private final ChatRoomMemberManager chatRoomMemberManager;

	@MessageMapping("/chat/message")
	public void messageHandler(ChatMessageRequest message) {
		switch (message.getType()) {
			case ENTER -> {
				message.setMessage(message.getSender() + "님이 입장하였습니다.");
				message.setMemberList(chatRoomMemberManager.getChatRoom(message.getRoomKey()));
			}
			case TALK -> {}
			case CODE -> {}
			case LEAVE -> {
				message.setMessage(message.getSender() + "님이 퇴장하였습니다.");
				message.setMemberList(chatRoomMemberManager.getChatRoom(message.getRoomKey()));
			}
			case EXPIRE -> {}
			default -> throw new BusinessException(CoffeeChatErrorCode.MESSAGE_TYPE_NOT_VALID);
		}
		kafkaTemplate.send("chat", message);
	}
}
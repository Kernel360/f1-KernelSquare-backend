package com.kernelsquare.memberapi.domain.coffeechat.controller;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kernelsquare.core.common_response.error.code.CoffeeChatErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.memberapi.domain.coffeechat.dto.ChatMessage;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MessageController {
	private final KafkaTemplate<String, ChatMessage> kafkaTemplate;

	@MessageMapping("/chat/message")
	public void messageHandler(ChatMessage message) {
		switch (message.getType()) {
			case ENTER -> message.setMessage(message.getSender() + "님이 입장하였습니다.");
			case LEAVE -> message.setMessage(message.getSender() + "님이 퇴장하였습니다.");
			case TALK -> {
			}
			default -> throw new BusinessException(CoffeeChatErrorCode.MESSAGE_TYPE_NOT_VALID);
		}
		kafkaTemplate.send("chat_" + message.getRoomKey(), message);
	}
}


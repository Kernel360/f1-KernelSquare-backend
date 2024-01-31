package com.kernelsquare.memberapi.domain.coffeechat.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.kernelsquare.core.common_response.error.code.CoffeeChatErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmongodb.domain.coffeechat.entity.MongoChatMessage;
import com.kernelsquare.domainmongodb.domain.coffeechat.repository.MongoChatMessageRepository;
import com.kernelsquare.memberapi.common.util.ChatMessageConverter;
import com.kernelsquare.memberapi.domain.coffeechat.dto.ChatMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaService {
	private final SimpMessageSendingOperations sendingOperations;
	private final MongoChatMessageRepository mongoChatMessageRepository;

	@KafkaListener(topicPattern = "chat_.*", groupId = "coffeechat")
	public void sendMessage(ChatMessage message) {
		try {
			MongoChatMessage recordMessage = ChatMessageConverter.toMongoChatMessage(message);
			mongoChatMessageRepository.save(recordMessage);
			sendingOperations.convertAndSend("/topic/chat/room/" + message.getRoomKey(), message);
		} catch (Exception e) {
			throw new BusinessException(CoffeeChatErrorCode.MESSAGE_DELIVERY_FAILED);
		}
	}
}
package com.kernelsquare.memberapi.domain.coffeechat.service;

import com.kernelsquare.memberapi.domain.coffeechat.dto.ChatMessageRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.kernelsquare.core.common_response.error.code.CoffeeChatErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmongodb.domain.coffeechat.entity.MongoChatMessage;
import com.kernelsquare.domainmongodb.domain.coffeechat.repository.MongoChatMessageRepository;
import com.kernelsquare.memberapi.common.util.ChatMessageConverter;
import com.kernelsquare.memberapi.domain.coffeechat.dto.ChatMessageResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
	private final SimpMessageSendingOperations sendingOperations;
	private final MongoChatMessageRepository mongoChatMessageRepository;

	@KafkaListener(topics = "chat", groupId = "coffeechat")
	public void sendMessage(ChatMessageRequest requestMessage) {
		try {
			MongoChatMessage recordMessage = ChatMessageConverter.toMongoChatMessage(requestMessage);
			mongoChatMessageRepository.save(recordMessage);

			ChatMessageResponse responseMessage = ChatMessageResponse.convertResponse(requestMessage);

			sendingOperations.convertAndSend("/topic/chat/room/" + responseMessage.getRoomKey(), responseMessage);
		} catch (Exception e) {
			throw new BusinessException(CoffeeChatErrorCode.MESSAGE_DELIVERY_FAILED);
		}
	}
}
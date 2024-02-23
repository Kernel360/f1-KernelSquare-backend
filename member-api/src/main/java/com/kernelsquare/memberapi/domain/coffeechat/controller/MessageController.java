package com.kernelsquare.memberapi.domain.coffeechat.controller;

import com.kernelsquare.core.common_response.error.code.CoffeeChatErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.stream.entity.KafkaMessage;
import com.kernelsquare.memberapi.domain.coffeechat.dto.ChatMessageRequest;
import com.kernelsquare.memberapi.domain.stream.dto.StreamDto;
import com.kernelsquare.memberapi.domain.stream.facade.StreamFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {
	private final StreamFacade streamFacade;

	@MessageMapping("/chat/message")
	public void messageHandler(ChatMessageRequest message) {
		switch (message.getType()) {
			case ENTER -> message.setMessage(message.getSender() + "님이 입장하였습니다.");
			case TALK -> {}
			case CODE -> {}
			case LEAVE -> message.setMessage(message.getSender() + "님이 퇴장하였습니다.");
			case EXPIRE -> {}
			default -> throw new BusinessException(CoffeeChatErrorCode.MESSAGE_TYPE_NOT_VALID);
		}

		var request = StreamDto.PublishRequest.builder()
				.topic(message.getRoomKey())
				.message(message)
				.topicPrefix(KafkaMessage.TopicPrefix.CHATTING)
				.build();

		streamFacade.sendKafka(request);
	}
}


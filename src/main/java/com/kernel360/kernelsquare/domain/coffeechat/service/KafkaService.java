package com.kernel360.kernelsquare.domain.coffeechat.service;

import com.kernel360.kernelsquare.domain.coffeechat.dto.ChatMessage;
import com.kernel360.kernelsquare.domain.coffeechat.entity.MongoChatMessage;
import com.kernel360.kernelsquare.domain.coffeechat.repository.MongoChatMessageRepository;
import com.kernel360.kernelsquare.global.common_response.error.code.CoffeeChatErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaService {
    private final SimpMessageSendingOperations sendingOperations;
    private final MongoChatMessageRepository mongoChatMessageRepository;

    @KafkaListener(topicPattern = "chat_.*", groupId = "coffeechat")
    public void sendMessage(ChatMessage message) {
        try {
            MongoChatMessage recordMessage = MongoChatMessage.from(message);
            mongoChatMessageRepository.save(recordMessage);
            sendingOperations.convertAndSend("/topic/chat/room/" + message.getRoomKey(), message);
        } catch (Exception e) {
            throw new BusinessException(CoffeeChatErrorCode.MESSAGE_DELIVERY_FAILED);
        }
    }
}

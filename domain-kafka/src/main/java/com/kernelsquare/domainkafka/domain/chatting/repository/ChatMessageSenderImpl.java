package com.kernelsquare.domainkafka.domain.chatting.repository;

import com.kernelsquare.domainkafka.domain.chatting.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static com.kernelsquare.domainkafka.constants.KafkaConstants.CHATTING_TOPIC;

@Component
@RequiredArgsConstructor
public class ChatMessageSenderImpl implements ChatMessageSender {
    private final KafkaTemplate<String, ChatMessage> kafkaTemplate;

    @Override
    public void send(ChatMessage chatMessage) {
        kafkaTemplate.send(CHATTING_TOPIC, chatMessage);
    }
}

package com.kernelsquare.domainkafka.domain.chatting.repository;

import com.kernelsquare.domainkafka.domain.chatting.entity.ChatMessage;

public interface ChatMessageSender {
    void send(ChatMessage chatMessage);
}

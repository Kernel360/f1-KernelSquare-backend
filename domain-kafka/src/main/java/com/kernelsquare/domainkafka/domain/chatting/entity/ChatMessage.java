package com.kernelsquare.domainkafka.domain.chatting.entity;

import com.kernelsquare.core.type.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private MessageType type;

    private String roomKey;

    private Long senderId;

    private String sender;

    private String senderImageUrl;

    private String message;

    private LocalDateTime sendTime;

    private List<?> memberList;

    public void setMessage(String message) {
        this.message = message;
    }
}

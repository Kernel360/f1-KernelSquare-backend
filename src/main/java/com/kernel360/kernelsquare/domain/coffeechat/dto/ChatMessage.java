package com.kernel360.kernelsquare.domain.coffeechat.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    public enum MessageType {
        ENTER, TALK, LEAVE, EXPIRE
    }

    private MessageType type;

    private String roomKey;

    private String sender;

    private String message;

    public void setMessage(String message) {
        this.message = message;
    }
}

package com.kernel360.kernelsquare.domain.coffeechat.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    private MessageType type;

    private String roomKey;

    private String sender;

    private String message;

    private LocalDateTime sendTime;
    public void setMessage(String message) {
        this.message = message;
    }
}

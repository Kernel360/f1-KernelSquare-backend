package com.kernelsquare.memberapi.domain.coffeechat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kernelsquare.domainmongodb.domain.coffeechat.entity.MongoChatMessage;
import com.kernelsquare.domainmongodb.domain.coffeechat.entity.MongoMessageType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FindMongoChatMessage(
    String roomKey,

    MongoMessageType type,

    Long senderId,

    String sender,

    String senderImageUrl,

    String message,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    LocalDateTime sendTime
) {
    public static FindMongoChatMessage from(MongoChatMessage message) {
        return FindMongoChatMessage.builder()
            .roomKey(message.getRoomKey())
            .type(message.getType())
            .senderId(message.getSenderId())
            .sender(message.getSender())
            .senderImageUrl(message.getSenderImageUrl())
            .message(message.getMessage())
            .sendTime(message.getSendTime())
            .build();
    }
}

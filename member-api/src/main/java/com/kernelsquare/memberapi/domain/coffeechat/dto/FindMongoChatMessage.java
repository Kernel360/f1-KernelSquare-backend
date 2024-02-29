package com.kernelsquare.memberapi.domain.coffeechat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kernelsquare.core.constants.TimeResponseFormat;
import com.kernelsquare.domainmongodb.domain.coffeechat.entity.MongoChatMessage;
import com.kernelsquare.domainmongodb.domain.coffeechat.entity.MongoMessageType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FindMongoChatMessage(
    MongoMessageType type,

    String roomKey,

    String sender,

    String message,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeResponseFormat.PATTERN)
    LocalDateTime sendTime
) {
    public static FindMongoChatMessage from(MongoChatMessage message) {
        return FindMongoChatMessage.builder()
            .type(message.getType())
            .roomKey(message.getRoomKey())
            .sender(message.getSender())
            .message(message.getMessage())
            .sendTime(message.getSendTime())
            .build();
    }
}

package com.kernel360.kernelsquare.domain.coffeechat.entity;

import com.kernel360.kernelsquare.domain.coffeechat.dto.ChatMessage;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

@Builder
@Getter
@Document(collection = "chatting")
public class MongoChatMessage {

    @Id
    private String id;

    @Indexed
    private String roomKey;

    private MongoMessageType type;

    private String sender;

    private String message;

    private LocalDateTime sendTime;

    public static MongoChatMessage from(ChatMessage message) {
        return MongoChatMessage.builder()
            .roomKey(message.getRoomKey())
            .type(MongoMessageType.valueOf(String.valueOf(message.getType())))
            .sender(message.getSender())
            .message(message.getMessage())
            .sendTime(message.getSendTime())
            .build();
    }
}

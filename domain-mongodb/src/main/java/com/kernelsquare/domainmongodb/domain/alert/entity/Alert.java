package com.kernelsquare.domainmongodb.domain.alert.entity;

import io.micrometer.common.util.StringUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Document(collection = "alert")
public class Alert {
    @Id
    private String id;

    @Indexed
    private String recipientId;

    private String recipient;

    private String senderId;

    private String sender;

    private String message;

    private AlertType alertType;

    private LocalDateTime sendTime;

    @Getter
    @RequiredArgsConstructor
    public enum AlertType {
        QUESTION_REPLY,
        RANK_ANSWER,
        COFFEE_CHAT_REQUEST
    }

    @Builder
    public Alert(String recipientId, String recipient, String senderId, String sender, String message, AlertType alertType) {
        if (StringUtils.isBlank(recipientId))
            throw new InvalidParameterException("Invalid recipientId");
        if (StringUtils.isBlank(recipient))
            throw new InvalidParameterException("Invalid recipient");
        if (StringUtils.isBlank(senderId))
            throw new InvalidParameterException("Invalid senderId");
        if (StringUtils.isBlank(sender))
            throw new InvalidParameterException("Invalid sender");
        if (StringUtils.isBlank(message))
            throw new InvalidParameterException("Invalid message");
        if (Objects.isNull(alertType))
            throw new InvalidParameterException("Invalid AlertType");

        this.recipientId = recipientId;
        this.recipient = recipient;
        this.senderId = senderId;
        this.sender = sender;
        this.message = message;
        this.alertType = alertType;
        this.sendTime = LocalDateTime.now();
    }
}

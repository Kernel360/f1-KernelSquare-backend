package com.kernelsquare.domainkafka.domain.alert.entity;

import com.kernelsquare.core.util.TokenGenerator;
import io.micrometer.common.util.StringUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

@Getter
public class Alert {
    private final String ALERT_PREFIX = "alt_";

    private String id;

    private String recipientId;

    private String recipient;

    private String senderId;

    private String sender;

    private AlertType alertType;

    private LocalDateTime sendTime;

    private Map<String, String> payload;

    @Getter
    @RequiredArgsConstructor
    public enum AlertType {
        QUESTION_REPLY,
        RANK_ANSWER,
        COFFEE_CHAT_REQUEST
    }

    @Builder
    public Alert(String recipientId, String recipient, String senderId, String sender, AlertType alertType,
                 Map<String, String> payload) {
        if (StringUtils.isBlank(recipientId))
            throw new InvalidParameterException("Invalid recipientId");
        if (StringUtils.isBlank(recipient))
            throw new InvalidParameterException("Invalid recipient");
        if (StringUtils.isBlank(senderId))
            throw new InvalidParameterException("Invalid senderId");
        if (StringUtils.isBlank(sender))
            throw new InvalidParameterException("Invalid sender");
        if (Objects.isNull(alertType))
            throw new InvalidParameterException("Invalid AlertType");

        this.id = TokenGenerator.randomCharacterWithPrefix(ALERT_PREFIX);
        this.recipientId = recipientId;
        this.recipient = recipient;
        this.senderId = senderId;
        this.sender = sender;
        this.alertType = alertType;
        this.sendTime = LocalDateTime.now();
        this.payload = payload;
    }
}

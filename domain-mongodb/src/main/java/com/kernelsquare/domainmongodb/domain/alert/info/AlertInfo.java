package com.kernelsquare.domainmongodb.domain.alert.info;

import com.kernelsquare.domainmongodb.domain.alert.entity.Alert;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AlertInfo {
    private final String recipientId;
    private final String recipient;
    private final String senderId;
    private final String sender;
    private final String message;
    private final Alert.AlertType alertType;
    private final LocalDateTime sendTime;

    @Builder
    public AlertInfo(Alert alert) {
        this.recipientId = alert.getRecipientId();
        this.recipient = alert.getRecipient();
        this.senderId = alert.getSenderId();
        this.sender = alert.getSender();
        this.message = alert.getMessage();
        this.alertType = alert.getAlertType();
        this.sendTime = alert.getSendTime();
    }

    public static AlertInfo of(Alert alert) {
        return AlertInfo.builder()
            .alert(alert)
            .build();
    }
}

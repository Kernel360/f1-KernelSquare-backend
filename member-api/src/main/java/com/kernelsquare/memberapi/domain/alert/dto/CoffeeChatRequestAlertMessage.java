package com.kernelsquare.memberapi.domain.alert.dto;

import com.kernelsquare.domainkafka.domain.alert.entity.Alert;
import lombok.Builder;

import java.util.HashMap;
import java.util.Map;

@Builder
public class CoffeeChatRequestAlertMessage implements AlertMessage {
    private final String recipientId;
    private final String recipient;
    private final String senderId;
    private final String sender;

    @Override
    public Alert process() {
        Map<String, String> payload = new HashMap<>(Map.of(
            "senderId", senderId,
            "sender", sender
        ));

        return Alert.builder()
            .recipientId(recipientId)
            .recipient(recipient)
            .senderId(senderId)
            .sender(sender)
            .alertType(Alert.AlertType.COFFEE_CHAT_REQUEST)
            .payload(payload)
            .build();
    }
}

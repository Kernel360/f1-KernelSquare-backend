package com.kernelsquare.memberapi.domain.alert.dto;

import com.kernelsquare.domainkafka.domain.alert.entity.Alert;
import lombok.Builder;

import java.util.HashMap;
import java.util.Map;

@Builder
public class QuestionReplyAlertMessage implements AlertMessage {
    private final String recipientId;
    private final String recipient;
    private final String senderId;
    private final String sender;
    private final String questionId;
    private final String questionTitle;

    @Override
    public Alert process() {
        Map<String, String> payload = new HashMap<>(Map.of(
            "questionId", questionId,
            "questionTitle", questionTitle,
            "sender", sender
        ));

        return Alert.builder()
            .recipientId(recipientId)
            .recipient(recipient)
            .senderId(senderId)
            .sender(sender)
            .alertType(Alert.AlertType.QUESTION_REPLY)
            .payload(payload)
            .build();
    }
}

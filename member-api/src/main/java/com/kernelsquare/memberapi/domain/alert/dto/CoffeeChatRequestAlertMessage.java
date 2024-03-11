package com.kernelsquare.memberapi.domain.alert.dto;

import com.kernelsquare.domainmongodb.domain.alert.entity.Alert;
import lombok.Builder;

@Builder
public class CoffeeChatRequestAlertMessage implements AlertMessage {
    private final String recipientId;
    private final String recipient;
    private final String senderId;
    private final String sender;

    @Override
    public Alert process() {
        return Alert.builder()
            .recipientId(recipientId)
            .recipient(recipient)
            .senderId(senderId)
            .sender(sender)
            .message("커피챗 요청이 들어왔습니다.")
            .alertType(Alert.AlertType.COFFEE_CHAT_REQUEST)
            .build();
    }
}

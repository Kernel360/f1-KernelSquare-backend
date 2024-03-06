package com.kernelsquare.memberapi.domain.alert.dto;

import com.kernelsquare.domainmongodb.domain.alert.entity.Alert;
import lombok.Builder;

@Builder
public class QuestionReplyAlertMessage implements AlertMessage {
    private final String recipientId;
    private final String recipient;
    private final String senderId;
    private final String sender;
    private final String questionTitle;

    @Override
    public Alert process() {
        return Alert.builder()
            .recipientId(recipientId)
            .recipient(recipient)
            .senderId(senderId)
            .sender(sender)
            .message(questionTitle + " 글에 " + sender + "님이 답변했습니다.")
            .alertType(Alert.AlertType.QUESTION_REPLY)
            .build();
    }
}

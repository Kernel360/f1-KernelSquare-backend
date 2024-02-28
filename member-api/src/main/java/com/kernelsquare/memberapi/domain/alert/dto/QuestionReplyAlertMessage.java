package com.kernelsquare.memberapi.domain.alert.dto;

import com.kernelsquare.domainmongodb.domain.alert.entity.Alert;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import lombok.Builder;

@Builder
public class QuestionReplyAlertMessage implements AlertMessage {
    private Question question;
    private String senderId;

    @Override
    public Alert process() {
        return Alert.builder()
            .recipientId(question.getMember().getId().toString())
            .senderId(senderId)
            .message(question.getTitle() + " 글에 답변이 달렸습니다.")
            .alertType(Alert.AlertType.QUESTION_REPLY)
            .build();
    }
}

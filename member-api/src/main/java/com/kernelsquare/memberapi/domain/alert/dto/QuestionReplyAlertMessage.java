package com.kernelsquare.memberapi.domain.alert.dto;

import com.kernelsquare.domainmongodb.domain.alert.entity.Alert;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import lombok.Builder;

@Builder
public class QuestionReplyAlertMessage implements AlertMessage {
    private Question question;
    private Member sender;

    @Override
    public Alert process() {
        return Alert.builder()
            .recipientId(question.getMember().getId().toString())
            .recipient(question.getMember().getNickname())
            .senderId(sender.getId().toString())
            .sender(sender.getNickname())
            .message(question.getTitle() + " 글에 " + sender.getNickname() + "님이 답변했습니다.")
            .alertType(Alert.AlertType.QUESTION_REPLY)
            .build();
    }
}

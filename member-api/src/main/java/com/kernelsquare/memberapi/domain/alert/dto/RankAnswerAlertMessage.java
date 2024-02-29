package com.kernelsquare.memberapi.domain.alert.dto;

import com.kernelsquare.domainmongodb.domain.alert.entity.Alert;
import com.kernelsquare.domainmysql.domain.answer.entity.Answer;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import com.kernelsquare.domainmysql.domain.rank.entity.Rank;
import lombok.Builder;

@Builder
public class RankAnswerAlertMessage implements AlertMessage {
    private Question question;
    private Answer answer;
    private Rank rank;

    @Override
    public Alert process() {
        return Alert.builder()
            .recipientId(answer.getMember().getId().toString())
            .recipient(answer.getMember().getNickname())
            .senderId("system")
            .sender("system")
            .message(question.getTitle() + " 글에 작성하신 답변이 " + rank.getName() + "등 답변이 되었습니다.")
            .alertType(Alert.AlertType.RANK_ANSWER)
            .build();
    }
}

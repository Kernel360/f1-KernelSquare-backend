package com.kernelsquare.memberapi.domain.alert.dto;

import com.kernelsquare.domainmysql.domain.answer.entity.Answer;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import com.kernelsquare.domainmysql.domain.rank.entity.Rank;
import lombok.Builder;

public class AlertDto {
    @Builder
    public record RankAnswerAlert(
        String recipientId,
        String recipient,
        String questionTitle,
        String rankName
    ) {
        public static RankAnswerAlert of(Question question, Answer answer, Rank rank) {
            return RankAnswerAlert.builder()
                .recipientId(answer.getMember().getId().toString())
                .recipient(answer.getMember().getNickname())
                .questionTitle(question.getTitle())
                .rankName(rank.getName().toString())
                .build();
        }
    }
}

package com.kernelsquare.memberapi.domain.alert.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kernelsquare.core.constants.TimeResponseFormat;
import com.kernelsquare.domainmongodb.domain.alert.entity.Alert;
import com.kernelsquare.domainmysql.domain.answer.entity.Answer;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import com.kernelsquare.domainmysql.domain.rank.entity.Rank;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public class AlertDto {
    @Builder
    public record FindAllResponse(
        String recipientId,
        String recipient,
        String senderId,
        String sender,
        String message,
        Alert.AlertType alertType,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeResponseFormat.PATTERN)
        LocalDateTime sendTime
    ) {}

    @Builder
    public record PersonalFindAllResponse(
        List<FindAllResponse> personalAlertList
    ) {
        public static PersonalFindAllResponse from(List<FindAllResponse> personalAlert) {
            return new PersonalFindAllResponse(
                personalAlert
            );
        }
    }

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

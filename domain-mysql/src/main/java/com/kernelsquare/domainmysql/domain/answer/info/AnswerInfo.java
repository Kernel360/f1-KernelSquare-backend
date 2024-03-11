package com.kernelsquare.domainmysql.domain.answer.info;

import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnswerInfo {
    private final String recipientId;
    private final String recipient;
    private final String senderId;
    private final String sender;
    private final String questionTitle;

    public static AnswerInfo from(Question question, Member answerAuthor) {
        return AnswerInfo.builder()
            .recipientId(question.getMember().getId().toString())
            .recipient(question.getMember().getNickname())
            .senderId(answerAuthor.getId().toString())
            .sender(answerAuthor.getNickname())
            .questionTitle(question.getTitle())
            .build();
    }
}

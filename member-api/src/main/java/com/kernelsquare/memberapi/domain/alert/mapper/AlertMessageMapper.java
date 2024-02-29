package com.kernelsquare.memberapi.domain.alert.mapper;

import com.kernelsquare.domainmysql.domain.answer.entity.Answer;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import com.kernelsquare.domainmysql.domain.question.repository.QuestionReader;
import com.kernelsquare.domainmysql.domain.rank.entity.Rank;
import com.kernelsquare.memberapi.domain.alert.dto.QuestionReplyAlertMessage;
import com.kernelsquare.memberapi.domain.alert.dto.RankAnswerAlertMessage;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlertMessageMapper {
    private final QuestionReader questionReader;

    public QuestionReplyAlertMessage of(Long questionId, MemberAdapter memberAdapter) {
        Question question = questionReader.findQuestion(questionId);
        Member sender = memberAdapter.getMember();
        return QuestionReplyAlertMessage.builder()
            .question(question)
            .sender(sender)
            .build();
    }

    public RankAnswerAlertMessage of(Question question, Answer answer, Rank rank) {
        return RankAnswerAlertMessage.builder()
            .question(question)
            .answer(answer)
            .rank(rank)
            .build();
    }
}

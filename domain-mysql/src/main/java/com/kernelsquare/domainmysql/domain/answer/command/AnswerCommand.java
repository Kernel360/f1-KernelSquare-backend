package com.kernelsquare.domainmysql.domain.answer.command;

import com.kernelsquare.core.util.ImageUtils;
import com.kernelsquare.domainmysql.domain.answer.entity.Answer;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import lombok.Builder;

public class AnswerCommand {
    @Builder
    public record CreateAnswer(
        Long questionId,
        String content,
        String imageUrl,
        Member author
    ) {
        public Answer toEntity(Question question) {
            return Answer.builder()
                .content(content)
                .imageUrl(ImageUtils.parseFilePath(imageUrl))
                .member(author)
                .question(question)
                .voteCount(0L)
                .build();
        }
    }
}
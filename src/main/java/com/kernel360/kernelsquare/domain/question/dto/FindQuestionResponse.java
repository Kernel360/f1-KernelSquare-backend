package com.kernel360.kernelsquare.domain.question.dto;

import com.kernel360.kernelsquare.domain.level.entity.Level;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.question.entity.Question;

import java.time.LocalDateTime;
import java.util.List;

public record FindQuestionResponse(
    Long id,
    String title,
    String content,
    String questionImageUrl,
    Long viewCount,
    Boolean closeStatus,
    String nickname,
    String memberImageUrl,
    Long level,
    String levelImageUrl,
    List<String> skills,
    LocalDateTime createdDate,
    LocalDateTime modifiedDate
//    List<Answer> answerList
) {
    public static FindQuestionResponse of (Member member, Question question, Level level) {
        return new FindQuestionResponse(
            question.getId(),
            question.getTitle(),
            question.getContent(),
            question.getImageUrl(),
            question.getViewCount(),
            question.getClosedStatus(),
            member.getNickname(),
            member.getImageUrl(),
            level.getName(),
            level.getImageUrl(),
            question.getTechStackList().stream().map(x -> x.getTechStack().getSkill()).toList(),
            question.getCreatedDate(),
            question.getModifiedDate()
        );
    }
}
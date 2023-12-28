package com.kernel360.kernelsquare.domain.answer.dto;

import com.kernel360.kernelsquare.domain.answer.entity.Answer;
import lombok.Builder;
import java.time.LocalDateTime;

public record FindAnswerResponse(
        Long answerId,
        Long questionId,
        String content,
        String rankImageUrl,
        String memberImageUrl,
        String createdBy,
        String answerImageUrl,
        LocalDateTime createdDate,
        LocalDateTime modifiedDate,
        Long voteCount
) {
    public static FindAnswerResponse from(Answer answer) {
        return new FindAnswerResponse(
                answer.getId(),
                answer.getQuestion().getId(),
                answer.getContent(),
                null,
                answer.getMember().getImageUrl(),
                answer.getMember().getNickname(),
                answer.getImageUrl(),
                answer.getCreatedDate(),
                answer.getModifiedDate(),
                answer.getVoteCount()
        );
    }
}

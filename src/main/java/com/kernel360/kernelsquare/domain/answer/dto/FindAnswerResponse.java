package com.kernel360.kernelsquare.domain.answer.dto;

import com.kernel360.kernelsquare.domain.answer.entity.Answer;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record FindAnswerResponse(
    Long id,
    Long questionId,
    String content,
    String rankImageUrl,
    String createdBy,
    String imageUrl,
    String createdDate,
    Long voteCount
) {
    public static FindAnswerResponse from(Answer answer) {
        return new FindAnswerResponse(
                answer.getId(),
                answer.getQuestion().getId(),
                answer.getContent(),
                "rankUrl",// answer.getRank().getImageUrl(),
                answer.getMember().getNickname(),
                answer.getImageUrl(),
                answer.getCreatedDate().toLocalDate().toString(),
                answer.getVoteCount()
        );
    }
}

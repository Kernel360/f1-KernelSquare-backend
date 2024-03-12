package com.kernelsquare.adminapi.domain.answer.dto;

import java.time.LocalDateTime;

import com.kernelsquare.core.util.ImageUtils;
import com.kernelsquare.domainmysql.domain.answer.entity.Answer;

public record FindAnswerResponse(
	Long answerId,
	Long questionId,
	String content,
	String rankImageUrl,
	String memberImageUrl,
	String createdBy,
	Long authorLevel,
	String answerImageUrl,
	LocalDateTime createdDate,
	LocalDateTime modifiedDate,
	Long voteCount,
	Long voteStatus
) {
	public static FindAnswerResponse from(Answer answer, String rankImageUrl, Long authorLevel, Long voteStatus) {
		return new FindAnswerResponse(
			answer.getId(),
			answer.getQuestion().getId(),
			answer.getContent(),
			ImageUtils.makeImageUrl(rankImageUrl),
			ImageUtils.makeImageUrl(answer.getMember().getImageUrl()),
			answer.getMember().getNickname(),
			authorLevel,
			ImageUtils.makeImageUrl(answer.getImageUrl()),
			answer.getCreatedDate(),
			answer.getModifiedDate(),
			answer.getVoteCount(),
			voteStatus
		);
	}
}

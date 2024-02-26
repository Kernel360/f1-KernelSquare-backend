package com.kernelsquare.memberapi.domain.answer.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
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
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
	LocalDateTime createdDate,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
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

package com.kernelsquare.memberapi.domain.answer.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kernelsquare.core.constants.TimeResponseFormat;
import com.kernelsquare.core.util.ImageUtils;
import com.kernelsquare.domainmysql.domain.answer.entity.Answer;

import com.kernelsquare.domainmysql.domain.rank.entity.Rank;
import lombok.Builder;

@Builder
public record FindAnswerResponse(
	Long answerId,
	Long answerMemberId,
	Long questionId,
	String content,
	@JsonInclude(JsonInclude.Include.NON_NULL)
	Long rankName,
	@JsonInclude(JsonInclude.Include.NON_NULL)
	String rankImageUrl,
	String memberImageUrl,
	String memberNickname,
	Long authorLevel,
	String answerImageUrl,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeResponseFormat.PATTERN)
	LocalDateTime createdDate,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeResponseFormat.PATTERN)
	LocalDateTime modifiedDate,
	Long voteCount,
	Long voteStatus
) {
	public static FindAnswerResponse from(Answer answer, Long voteStatus) {
		return FindAnswerResponse
			.builder()
			.answerId(answer.getId())
			.answerMemberId(answer.getMember().getId())
			.questionId(answer.getQuestion().getId())
			.content(answer.getContent())
			.rankName(answer.getNullableRankName())
			.rankImageUrl(ImageUtils.makeImageUrl(answer.getNullableRankImageUrl()))
			.memberImageUrl(ImageUtils.makeImageUrl(answer.getMember().getImageUrl()))
			.memberNickname(answer.getMember().getNickname())
			.authorLevel(answer.getMember().getLevel().getName())
			.answerImageUrl(ImageUtils.makeImageUrl(answer.getImageUrl()))
			.createdDate(answer.getCreatedDate())
			.modifiedDate(answer.getModifiedDate())
			.voteCount(answer.getVoteCount())
			.voteStatus(voteStatus)
			.build();
	}
}

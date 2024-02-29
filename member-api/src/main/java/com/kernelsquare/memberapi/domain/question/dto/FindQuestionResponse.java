package com.kernelsquare.memberapi.domain.question.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kernelsquare.core.util.ImageUtils;
import com.kernelsquare.core.constants.TimeResponseFormat;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.question.entity.Question;

public record FindQuestionResponse(
	Long id,
	String title,
	String content,
	String questionImageUrl,
	Long viewCount,
	Boolean closeStatus,
	Long memberId,
	String nickname,
	String memberImageUrl,
	Long level,
	String levelImageUrl,
	List<String> skills,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeResponseFormat.PATTERN)
	LocalDateTime createdDate,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeResponseFormat.PATTERN)
	LocalDateTime modifiedDate
) {
	public static FindQuestionResponse of(Member member, Question question, Level level) {
		return new FindQuestionResponse(
			question.getId(),
			question.getTitle(),
			question.getContent(),
			ImageUtils.makeImageUrl(question.getImageUrl()),
			question.getViewCount(),
			question.getClosedStatus(),
			member.getId(),
			member.getNickname(),
			ImageUtils.makeImageUrl(member.getImageUrl()),
			level.getName(),
			ImageUtils.makeImageUrl(level.getImageUrl()),
			question.getTechStackList().stream().map(x -> x.getTechStack().getSkill()).toList(),
			question.getCreatedDate(),
			question.getModifiedDate()
		);
	}
}

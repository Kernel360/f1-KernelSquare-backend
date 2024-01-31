package com.kernelsquare.adminapi.domain.question.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.kernelsquare.adminapi.domain.image.utils.ImageUtils;
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
	LocalDateTime createdDate,
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

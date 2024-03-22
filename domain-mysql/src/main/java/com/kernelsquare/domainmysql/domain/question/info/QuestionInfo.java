package com.kernelsquare.domainmysql.domain.question.info;

import java.time.LocalDateTime;
import java.util.List;

import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import com.kernelsquare.domainmysql.domain.question_tech_stack.entity.QuestionTechStack;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Getter
public class QuestionInfo {
	private final Long id;
	private final String title;
	private final String content;
	private final String imageUrl;
	private final Long viewCount;
	private final Boolean closedStatus;
	private final Member member;
	private final List<QuestionTechStack> techStackList;
	private final LocalDateTime createdDate;
	private final LocalDateTime modifiedDate;

	@Builder
	public QuestionInfo(Question question) {
		this.id = question.getId();
		this.title = question.getTitle();
		this.content = question.getContent();
		this.imageUrl = question.getImageUrl();
		this.viewCount = question.getViewCount();
		this.closedStatus = question.getClosedStatus();
		this.member = question.getMember();
		this.techStackList = question.getTechStackList();
		this.createdDate = question.getCreatedDate();
		this.modifiedDate = question.getModifiedDate();
	}

	public static QuestionInfo of(Question question) {
		return QuestionInfo.builder()
			.question(question)
			.build();
	}

	@Getter
	public static class FindAllQuestionsInfo {
		private final Long id;
		private final String title;
		private final String imageUrl;
		private final Long viewCount;
		private final Boolean closedStatus;
		private final Long memberId;
		private final String nickname;
		private final String memberImageUrl;
		private final Long level;
		private final String levelImageUrl;
		private final LocalDateTime createdDate;
		private final LocalDateTime modifiedDate;
		private final String skills;

		@QueryProjection
		public FindAllQuestionsInfo(Long id, String title, String imageUrl, Long viewCount, Boolean closedStatus,
								Long memberId, String nickname, String memberImageUrl, Long level, String levelImageUrl,
								LocalDateTime createdDate, LocalDateTime modifiedDate, String skills) {
			this.id = id;
			this.title = title;
			this.imageUrl = imageUrl;
			this.viewCount = viewCount;
			this.closedStatus = closedStatus;
			this.memberId = memberId;
			this.nickname = nickname;
			this.memberImageUrl = memberImageUrl;
			this.level = level;
			this.levelImageUrl = levelImageUrl;
			this.createdDate = createdDate;
			this.modifiedDate = modifiedDate;
			this.skills = skills;
		}
	}
}

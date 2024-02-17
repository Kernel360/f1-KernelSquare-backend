package com.kernelsquare.domainmysql.domain.notice.entity;

import org.apache.commons.lang3.StringUtils;

import com.kernelsquare.core.common_response.error.exception.InvalidParamException;
import com.kernelsquare.core.util.TokenGenerator;
import com.kernelsquare.domainmysql.domain.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity(name = "Notice")
@Table(name = "notice")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseEntity {
	private final String NOTICE_PREFIX = "ntc_";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, name = "notice_token", columnDefinition = "bigint")
	private String noticeToken;

	@Column(nullable = false, name = "notice_title", columnDefinition = "varchar(50)")
	private String noticeTitle;

	@Column(nullable = false, name = "notice_content", columnDefinition = "text")
	private String noticeContent;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name = "notice_category", columnDefinition = "varchar(20)")
	private NoticeCategory noticeCategory;

	@Getter
	@RequiredArgsConstructor
	public enum NoticeCategory {
		GENERAL("일반 공지"),
		QNA("Q&A 관련 공지"),
		COFFEE_CHAT("커피챗 관련 공지");

		private final String description;
	}

	@Builder
	public Notice(String noticeTitle, String noticeContent, NoticeCategory noticeCategory) {
		if (StringUtils.isBlank(noticeTitle))
			throw new InvalidParamException("Notice.noticeTitle");
		if (StringUtils.isBlank(noticeContent))
			throw new InvalidParamException("Notice.noticeContent");
		if (noticeCategory == null)
			throw new InvalidParamException("Notice.noticeCategory");

		this.noticeToken = TokenGenerator.randomCharacterWithPrefix(NOTICE_PREFIX);
		this.noticeTitle = noticeTitle;
		this.noticeContent = noticeContent;
		this.noticeCategory = noticeCategory;
	}
}

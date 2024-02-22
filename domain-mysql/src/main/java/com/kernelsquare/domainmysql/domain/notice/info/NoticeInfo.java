package com.kernelsquare.domainmysql.domain.notice.info;

import java.time.LocalDateTime;

import com.kernelsquare.domainmysql.domain.notice.entity.Notice;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NoticeInfo {
	private final Long id;
	private final String noticeToken;
	private final String noticeTitle;
	private final String noticeContent;
	private final Notice.NoticeCategory noticeCategory;
	private final LocalDateTime createdDate;
	private final LocalDateTime modifiedDate;

	@Builder
	public NoticeInfo(Notice notice) {
		this.id = notice.getId();
		this.noticeToken = notice.getNoticeToken();
		this.noticeTitle = notice.getNoticeTitle();
		this.noticeContent = notice.getNoticeContent();
		this.noticeCategory = notice.getNoticeCategory();
		this.createdDate = notice.getCreatedDate();
		this.modifiedDate = notice.getModifiedDate();
	}

	public static NoticeInfo of(Notice notice) {
		return NoticeInfo.builder()
			.notice(notice)
			.build();
	}
}

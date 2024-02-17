package com.kernelsquare.domainmysql.domain.notice.info;

import com.kernelsquare.domainmysql.domain.notice.entity.Notice;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NoticeInfo {
	private Long id;
	private String noticeToken;
	private String noticeTitle;
	private String noticeContent;
	private Notice.NoticeCategory noticeCategory;

	@Builder
	public NoticeInfo(Notice notice) {
		this.id = notice.getId();
		this.noticeToken = notice.getNoticeToken();
		this.noticeTitle = notice.getNoticeTitle();
		this.noticeContent = notice.getNoticeContent();
		this.noticeCategory = notice.getNoticeCategory();
	}
}

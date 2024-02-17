package com.kernelsquare.domainmysql.domain.notice.command;

import com.kernelsquare.domainmysql.domain.notice.entity.Notice;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NoticeCommand {
	private String noticeTitle;
	private String noticeContent;
	private Notice.NoticeCategory noticeCategory;

	public Notice toEntity() {
		return Notice.builder()
			.noticeTitle(noticeTitle)
			.noticeContent(noticeContent)
			.noticeCategory(noticeCategory)
			.build();
	}
}

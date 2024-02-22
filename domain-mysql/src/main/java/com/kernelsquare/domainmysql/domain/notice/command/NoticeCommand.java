package com.kernelsquare.domainmysql.domain.notice.command;

import com.kernelsquare.domainmysql.domain.notice.entity.Notice;

import lombok.Builder;

public class NoticeCommand {
	@Builder
	public record CreateCommand(
		String noticeTitle,
		String noticeContent,
		Notice.NoticeCategory noticeCategory
	) {
		public Notice toEntity() {
			return Notice.builder()
				.noticeTitle(noticeTitle)
				.noticeContent(noticeContent)
				.noticeCategory(noticeCategory)
				.build();
		}
	}

	public record UpdateCommand(
		String noticeTitle,
		String noticeContent
	) {
	}
}

package com.kernelsquare.memberapi.domain.notice.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kernelsquare.core.constants.TimeResponseFormat;
import com.kernelsquare.domainmysql.domain.notice.entity.Notice;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public class NoticeDto {

	@Builder
	public record FindResponse(
		String noticeTitle,
		String noticeToken,
		String noticeContent,
		Notice.NoticeCategory noticeCategory,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeResponseFormat.PATTERN)
		LocalDateTime createdDate,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeResponseFormat.PATTERN)
		LocalDateTime modifiedDate
	) {
	}

	@Builder
	public record FindAllResponse(
		String noticeTitle,
		String noticeToken,
		Notice.NoticeCategory noticeCategory,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeResponseFormat.PATTERN)
		LocalDateTime createdDate,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeResponseFormat.PATTERN)
		LocalDateTime modifiedDate
	) {
	}
}

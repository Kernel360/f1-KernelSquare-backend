package com.kernelsquare.memberapi.domain.notice.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kernelsquare.domainmysql.domain.notice.entity.Notice;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public class NoticeDto {
	@Builder
	public record FindRequest(
		@NotBlank(message = "공지 토큰은 필수 입력 값입니다.")
		String noticeToken
	) {
	}

	@Builder
	public record SingleResponse(
		String noticeTitle,
		String noticeToken,
		String noticeContent,
		Notice.NoticeCategory noticeCategory,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
		LocalDateTime createdDate,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
		LocalDateTime modifiedDate
	) {
	}

	@Builder
	public record FindAllResponse(
		String noticeTitle,
		String noticeToken,
		Notice.NoticeCategory noticeCategory,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
		LocalDateTime createdDate,
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
		LocalDateTime modifiedDate
	) {
	}
}

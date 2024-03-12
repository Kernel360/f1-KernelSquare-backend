package com.kernelsquare.adminapi.domain.notice.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kernelsquare.core.constants.TimeResponseFormat;
import com.kernelsquare.core.validation.annotations.EnumValue;
import com.kernelsquare.domainmysql.domain.notice.entity.Notice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public class NoticeDto {
	@Builder
	public record CreateRequest(
		@NotBlank(message = "공지 제목은 필수 입력사항입니다.")
		@Size(min = 5, max = 100, message = "공지 제목 길이는 5자 이상 50자 이하로 작성해 주세요")
		String noticeTitle,

		@NotBlank(message = "공지 내용은 필수 입력사항입니다.")
		@Size(min = 10, max = 10000, message = "공지 내용은 10자 이상 1000자 이하로 작성해 주세요")
		String noticeContent,

		@EnumValue(enumClass = Notice.NoticeCategory.class, message = "유효한 카테고리를 선택해주세요")
		String noticeCategory
	) {
	}

	@Builder
	public record UpdateRequest(
		@NotBlank(message = "공지 제목은 필수 입력사항입니다.")
		@Size(min = 5, max = 50, message = "공지 제목 길이는 5자 이상 50자 이하로 작성해 주세요")
		String noticeTitle,
		@NotBlank(message = "공지 토큰은 필수 입력 값입니다.")
		String noticeToken,
		@NotBlank(message = "공지 내용은 필수 입력사항입니다.")
		@Size(min = 10, max = 1000, message = "공지 내용은 10자 이상 1000자 이하로 작성해 주세요")
		String noticeContent
	) {
	}

	@Builder
	public record UpdateCategoryRequest(
		@NotBlank(message = "공지 토큰은 필수 입력 값입니다.")
		String noticeToken
	) {
	}

	@Builder
	public record DeleteRequest(
		@NotBlank(message = "공지 토큰은 필수 입력 값입니다.")
		String noticeToken
	) {
	}

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

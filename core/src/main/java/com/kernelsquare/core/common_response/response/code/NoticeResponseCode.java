package com.kernelsquare.core.common_response.response.code;

import org.springframework.http.HttpStatus;

import com.kernelsquare.core.common_response.service.code.NoticeServiceStatus;
import com.kernelsquare.core.common_response.service.code.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NoticeResponseCode implements ResponseCode {
	NOTICE_FOUND(HttpStatus.OK, NoticeServiceStatus.NOTICE_FOUND, "단일 공지 조회 성공"),
	NOTICE_ALL_FOUND(HttpStatus.OK, NoticeServiceStatus.NOTICE_ALL_FOUND, "모든 공지 조회 성공"),
	NOTICE_UPDATED(HttpStatus.OK, NoticeServiceStatus.NOTICE_UPDATED, "공지 수정 성공"),
	NOTICE_DELETED(HttpStatus.OK, NoticeServiceStatus.NOTICE_DELETED, "공지 삭제 성공"),
	NOTICE_CREATED(HttpStatus.OK, NoticeServiceStatus.NOTICE_CREATED, "공지 생성 성공");
	private final HttpStatus httpStatus;
	private final ServiceStatus serviceStatus;
	private final String msg;

	@Override
	public HttpStatus getStatus() {
		return httpStatus;
	}

	@Override
	public Integer getCode() {
		return serviceStatus.getServiceStatus();
	}

	@Override
	public String getMsg() {
		return msg;
	}
}

package com.kernelsquare.core.common_response.error.code;

import org.springframework.http.HttpStatus;

import com.kernelsquare.core.common_response.service.code.HashtagServiceStatus;
import com.kernelsquare.core.common_response.service.code.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum HashtagErrorCode implements ErrorCode {
	HASHTAG_NOT_FOUND(HttpStatus.NOT_FOUND, HashtagServiceStatus.HASHTAG_NOT_FOUND, "해시 태그를 찾을 수 없습니다.");

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

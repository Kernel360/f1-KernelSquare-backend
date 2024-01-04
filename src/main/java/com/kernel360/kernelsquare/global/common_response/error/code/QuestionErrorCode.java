package com.kernel360.kernelsquare.global.common_response.error.code;

import org.springframework.http.HttpStatus;

import com.kernel360.kernelsquare.global.common_response.service.code.QuestionServiceStatus;
import com.kernel360.kernelsquare.global.common_response.service.code.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum QuestionErrorCode implements ErrorCode {
	QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, QuestionServiceStatus.QUESTION_NOT_FOUND, "존재하지 않는 질문"),
	PAGE_NOT_FOUND(HttpStatus.NOT_FOUND, QuestionServiceStatus.PAGE_NOT_FOUND, "존재하지 않는 페이지");

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

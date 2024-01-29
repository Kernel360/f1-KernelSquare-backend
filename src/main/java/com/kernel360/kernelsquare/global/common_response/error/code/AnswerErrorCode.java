package com.kernel360.kernelsquare.global.common_response.error.code;

import org.springframework.http.HttpStatus;

import com.kernel360.kernelsquare.global.common_response.service.code.AnswerServiceStatus;
import com.kernel360.kernelsquare.global.common_response.service.code.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AnswerErrorCode implements ErrorCode {
	ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, AnswerServiceStatus.ANSWER_NOT_FOUND, "존재하지 않는 답변");
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

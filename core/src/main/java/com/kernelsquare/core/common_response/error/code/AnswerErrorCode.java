package com.kernelsquare.core.common_response.error.code;

import org.springframework.http.HttpStatus;

import com.kernelsquare.core.common_response.service.code.AnswerServiceStatus;
import com.kernelsquare.core.common_response.service.code.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AnswerErrorCode implements ErrorCode {
	ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, AnswerServiceStatus.ANSWER_NOT_FOUND, "존재하지 않는 답변"),
	ANSWER_SELF_IMPOSSIBLE(HttpStatus.BAD_REQUEST, AnswerServiceStatus.ANSWER_SELF_IMPOSSIBLE , "본인 질문에 답변을 달 수 없습니다.");

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

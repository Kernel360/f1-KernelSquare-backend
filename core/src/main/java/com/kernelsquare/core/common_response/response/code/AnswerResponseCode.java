package com.kernelsquare.core.common_response.response.code;

import org.springframework.http.HttpStatus;

import com.kernelsquare.core.common_response.service.code.AnswerServiceStatus;
import com.kernelsquare.core.common_response.service.code.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AnswerResponseCode implements ResponseCode {
	ANSWER_CREATION_NOT_AUTHORIZED(HttpStatus.FORBIDDEN,
		AnswerServiceStatus.ANSWER_CREATION_NOT_AUTHORIZED, "답변을 입력할 권한이 없습니다."),
	ANSWER_UPDATE_NOT_AUTHORIZED(HttpStatus.FORBIDDEN,
		AnswerServiceStatus.ANSWER_UPDATE_NOT_AUTHORIZED, "답변을 수정할 권한이 없습니다."),

	ANSWER_CREATED(HttpStatus.OK, AnswerServiceStatus.ANSWER_CREATED, "답변 생성 성공"),
	ANSWERS_ALL_FOUND(HttpStatus.OK, AnswerServiceStatus.ANSWERS_ALL_FOUND, "질문에 대한 모든 답변 조회 성공"),
	ANSWER_UPDATED(HttpStatus.OK, AnswerServiceStatus.ANSWER_UPDATED, "답변 수정 성공"),
	ANSWER_DELETED(HttpStatus.OK, AnswerServiceStatus.ANSWER_DELETED, "답변 삭제 성공");

	private final HttpStatus code;
	private final ServiceStatus serviceStatus;
	private final String msg;

	@Override
	public HttpStatus getStatus() {
		return code;
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

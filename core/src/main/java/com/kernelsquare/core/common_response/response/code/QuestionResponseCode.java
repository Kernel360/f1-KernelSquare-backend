package com.kernelsquare.core.common_response.response.code;

import org.springframework.http.HttpStatus;

import com.kernelsquare.core.common_response.service.code.QuestionServiceStatus;
import com.kernelsquare.core.common_response.service.code.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum QuestionResponseCode implements ResponseCode {
	QUESTION_CREATED(HttpStatus.OK, QuestionServiceStatus.QUESTION_CREATED, "질문 생성 성공"),
	QUESTION_FOUND(HttpStatus.OK, QuestionServiceStatus.QUESTION_FOUND, "질문 조회 성공"),
	QUESTION_ALL_FOUND(HttpStatus.OK, QuestionServiceStatus.QUESTION_ALL_FOUND, "모든 질문 조회 성공"),
	QUESTION_UPDATED(HttpStatus.OK, QuestionServiceStatus.QUESTION_UPDATED, "질문 수정 성공"),
	QUESTION_DELETED(HttpStatus.OK, QuestionServiceStatus.QUESTION_DELETED, "질문 삭제 성공");

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

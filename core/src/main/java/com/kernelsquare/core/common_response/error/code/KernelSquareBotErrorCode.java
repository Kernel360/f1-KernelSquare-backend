package com.kernelsquare.core.common_response.error.code;

import org.springframework.http.HttpStatus;

import com.kernelsquare.core.common_response.service.code.KernelSquareBotServiceStatus;
import com.kernelsquare.core.common_response.service.code.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum KernelSquareBotErrorCode implements ErrorCode {
	ANSWER_ALREADY_EXIST(HttpStatus.CONFLICT, KernelSquareBotServiceStatus.ANSWER_ALREADY_EXIST, "이미 AI 인턴이 답변한 질문이에요."),
	EMPTY_ANSWER_RESPONSE(HttpStatus.NOT_FOUND, KernelSquareBotServiceStatus.EMPTY_ANSWER_RESPONSE,
		"AI 인턴이 답변을 할 수 없는 상태에요."),
	QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, KernelSquareBotServiceStatus.QUESTION_NOT_FOUND, "해당 질문이 존재하지 않습니다."),
	KERNEL_SQUARE_BOT_NOT_FOUND(HttpStatus.NOT_FOUND, KernelSquareBotServiceStatus.KERNEL_SQUARE_BOT_NOT_FOUND, "AI 인턴이 퇴사했어요.");

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

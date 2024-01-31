package com.kernelsquare.core.common_response.error.code;

import org.springframework.http.HttpStatus;

import com.kernelsquare.core.common_response.service.code.AuthServiceStatus;
import com.kernelsquare.core.common_response.service.code.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
	INVALID_ACCOUNT(HttpStatus.UNAUTHORIZED, AuthServiceStatus.INVALID_ACCOUNT, "계정정보가 일치하지 않습니다."),
	INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, AuthServiceStatus.INVALID_PASSWORD, "비밀번호가 일치하지 않습니다."),
	ALREADY_SAVED_NICKNAME(HttpStatus.CONFLICT, AuthServiceStatus.ALREADY_SAVED_NICKNAME, "사용 중인 닉네임입니다."),
	ALREADY_SAVED_EMAIL(HttpStatus.CONFLICT, AuthServiceStatus.ALREADY_SAVED_EMAIL, "사용 중인 이메일입니다."),
	UNAUTHORIZED_ACCESS(HttpStatus.NOT_FOUND, AuthServiceStatus.UNAUTHORIZED_ACCESS, "접근 권한이 없습니다."),
	UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, AuthServiceStatus.UNAUTHENTICATED, "해당 기능은 로그인이 필요합니다.");

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


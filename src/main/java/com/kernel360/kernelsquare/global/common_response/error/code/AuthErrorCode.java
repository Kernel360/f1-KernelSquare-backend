package com.kernel360.kernelsquare.global.common_response.error.code;

import org.springframework.http.HttpStatus;

import com.kernel360.kernelsquare.global.common_response.service.code.AuthServiceStatus;
import com.kernel360.kernelsquare.global.common_response.service.code.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
	INVALID_ACCOUNT(HttpStatus.UNAUTHORIZED, AuthServiceStatus.INVALID_ACCOUNT, "계정정보가 일치하지 않습니다."),
	INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, AuthServiceStatus.INVALID_PASSWORD, "비밀번호가 일치하지 않습니다."),
	ALREADY_SAVED_NICKNAME(HttpStatus.CONFLICT, AuthServiceStatus.ALREADY_SAVED_NICKNAME, "사용 중인 닉네임입니다."),
	ALREADY_SAVED_EMAIL(HttpStatus.CONFLICT, AuthServiceStatus.ALREADY_SAVED_EMAIL, "사용 중인 이메일입니다.");

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


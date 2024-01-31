package com.kernelsquare.core.common_response.response.code;

import org.springframework.http.HttpStatus;

import com.kernelsquare.core.common_response.service.code.AuthServiceStatus;
import com.kernelsquare.core.common_response.service.code.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AuthResponseCode implements ResponseCode {
	LOGIN_SUCCESS(HttpStatus.OK, AuthServiceStatus.LOGIN_SUCCESS, "로그인 성공"),
	SIGN_UP_SUCCESS(HttpStatus.OK, AuthServiceStatus.SIGN_UP_SUCCESS, "회원 가입 성공"),
	EMAIL_UNIQUE_VALIDATED(HttpStatus.OK, AuthServiceStatus.EMAIL_UNIQUE_VALIDATED, "이메일 중복 확인 성공"),
	NICKNAME_UNIQUE_VALIDATED(HttpStatus.OK, AuthServiceStatus.NICKNAME_UNIQUE_VALIDATED, "닉네임 중복 확인 성공"),
	ACCESS_TOKEN_REISSUED(HttpStatus.OK, AuthServiceStatus.ACCESS_TOKEN_REISSUED, "액세스 토큰 재발급 성공"),
	LOGOUT_SUCCESS(HttpStatus.OK, AuthServiceStatus.LOGOUT_SUCCESS, "로그아웃 성공");

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

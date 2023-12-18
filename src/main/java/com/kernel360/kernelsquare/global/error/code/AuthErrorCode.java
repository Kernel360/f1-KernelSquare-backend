package com.kernel360.kernelsquare.global.error.code;

import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
	INVALID_ACCOUNT(HttpStatus.UNAUTHORIZED, "계정정보가 일치하지 않습니다."),
	INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
	ALREADY_SAVED_NICKNAME(HttpStatus.CONFLICT, "사용 중인 닉네임입니다."),
	ALREADY_SAVED_EMAIL(HttpStatus.CONFLICT, "사용 중인 이메일입니다."),
	ALREADY_SOCIAL_LOGIN(HttpStatus.CONFLICT, "소셜로 가입한 회원입니다. 소셜로 로그인 해주세요.");

	private final HttpStatus code;
	private final String msg;

	@Override
	public HttpStatus getStatus() {
		return code;
	}

	@Override
	public String getMsg() {
		return msg;
	}
}


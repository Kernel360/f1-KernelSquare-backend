package com.kernelsquare.core.common_response.error.code;

import org.springframework.http.HttpStatus;

import com.kernelsquare.core.common_response.service.code.MemberServiceStatus;
import com.kernelsquare.core.common_response.service.code.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, MemberServiceStatus.MEMBER_NOT_FOUND, "존재하지 않는 회원입니다."),
	AUTHORITY_TYPE_NOT_VALID(HttpStatus.BAD_REQUEST, MemberServiceStatus.AUTHORITY_TYPE_NOT_VALID, "유효하지 않는 권한 타입입니다.");

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

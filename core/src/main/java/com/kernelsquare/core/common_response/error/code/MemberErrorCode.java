package com.kernelsquare.core.common_response.error.code;

import org.springframework.http.HttpStatus;

import com.kernelsquare.core.common_response.service.code.MemberServiceStatus;
import com.kernelsquare.core.common_response.service.code.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, MemberServiceStatus.MEMBER_NOT_FOUND, "존재하지 않는 회원입니다.");

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

package com.kernel360.kernelsquare.global.common_response.error.code;

import org.springframework.http.HttpStatus;

import com.kernel360.kernelsquare.global.common_response.service.code.AuthorityServiceStatus;
import com.kernel360.kernelsquare.global.common_response.service.code.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AuthorityErrorCode implements ErrorCode {
	AUTHORITY_NOT_FOUND(HttpStatus.NOT_FOUND, AuthorityServiceStatus.AUTHORITY_NOT_FOUND, "권한이 존재하지 않습니다.");

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

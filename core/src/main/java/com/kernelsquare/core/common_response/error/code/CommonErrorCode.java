package com.kernelsquare.core.common_response.error.code;

import org.springframework.http.HttpStatus;

import com.kernelsquare.core.common_response.service.code.CommonServiceStatus;
import com.kernelsquare.core.common_response.service.code.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {
	DUPLICATE_DATA_EXIST(HttpStatus.CONFLICT, CommonServiceStatus.DUPLICATE_DATA_EXIST, "데이터 중복입니다"),
	INVALID_PARAMETER(HttpStatus.OK, CommonServiceStatus.INVALID_PARAMETER, "요청한 값이 올바르지 않습니다.");

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

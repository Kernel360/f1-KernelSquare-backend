package com.kernelsquare.core.common_response.error.code;

import org.springframework.http.HttpStatus;

import com.kernelsquare.core.common_response.service.code.CategoryServiceStatus;
import com.kernelsquare.core.common_response.service.code.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CategoryErrorCode implements ErrorCode {
	CATEGORY_NOT_VALID(HttpStatus.BAD_REQUEST, CategoryServiceStatus.CATEGORY_NOT_VALID, "유효하지 않는 카테고리");

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

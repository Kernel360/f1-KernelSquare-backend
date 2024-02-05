package com.kernelsquare.core.common_response.error.code;

import org.springframework.http.HttpStatus;

import com.kernelsquare.core.common_response.service.code.ServiceStatus;
import com.kernelsquare.core.common_response.service.code.TechStackServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TechStackErrorCode implements ErrorCode {
	TECH_STACK_NOT_FOUND(HttpStatus.NOT_FOUND, TechStackServiceStatus.TECH_STACK_NOT_FOUND, "존재하지 않는 기술 스택"),
	TECH_STACK_ALREADY_EXISTED(HttpStatus.CONFLICT, TechStackServiceStatus.TECH_STACK_ALREADY_EXISTED, "이미 존재하는 기술 스택"),
	PAGE_NOT_FOUND(HttpStatus.NOT_FOUND, TechStackServiceStatus.PAGE_NOT_FOUND, "존재하지 않는 페이지");

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

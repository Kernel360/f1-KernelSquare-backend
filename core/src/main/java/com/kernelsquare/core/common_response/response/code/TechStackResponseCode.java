package com.kernelsquare.core.common_response.response.code;

import org.springframework.http.HttpStatus;

import com.kernelsquare.core.common_response.service.code.ServiceStatus;
import com.kernelsquare.core.common_response.service.code.TechStackServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TechStackResponseCode implements ResponseCode {
	TECH_STACK_CREATED(HttpStatus.OK, TechStackServiceStatus.TECH_STACK_CREATED, "기술 스택 생성 성공"),
	TECH_STACK_ALL_FOUND(HttpStatus.OK, TechStackServiceStatus.TECH_STACK_ALL_FOUND, "기술 스택 모든 조회 성공"),
	TECH_STACK_UPDATED(HttpStatus.OK, TechStackServiceStatus.TECH_STACK_UPDATED, "기술 스택 수정 성공"),
	TECH_STACK_DELETED(HttpStatus.OK, TechStackServiceStatus.TECH_STACK_DELETED, "기술 스택 삭제 성공");

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

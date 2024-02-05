package com.kernelsquare.core.common_response.response.code;

import org.springframework.http.HttpStatus;

import com.kernelsquare.core.common_response.service.code.SearchServiceStatus;
import com.kernelsquare.core.common_response.service.code.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SearchResponseCode implements ResponseCode {
	SEARCH_QUESTION_COMPLETED(HttpStatus.OK, SearchServiceStatus.SEARCH_QUESTION_COMPLETED, "질문 검색 성공"),
	SEARCH_TECH_STACK_COMPLETED(HttpStatus.OK, SearchServiceStatus.SEARCH_TECH_STACK_COMPLETED, "기술 스택 검색 성공");

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

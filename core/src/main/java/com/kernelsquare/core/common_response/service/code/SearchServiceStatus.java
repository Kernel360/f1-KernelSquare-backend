package com.kernelsquare.core.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SearchServiceStatus implements ServiceStatus {
	//error

	//success
	SEARCH_QUESTION_COMPLETED(2540),
	SEARCH_TECH_STACK_COMPLETED(2541);

	private final Integer code;

	@Override
	public Integer getServiceStatus() {
		return code;
	}
}

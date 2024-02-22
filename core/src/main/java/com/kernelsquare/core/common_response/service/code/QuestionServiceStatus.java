package com.kernelsquare.core.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum QuestionServiceStatus implements ServiceStatus {
	//error
	QUESTION_NOT_FOUND(2100),
	PAGE_NOT_FOUND(2101),
	TOO_LONG_KEYWORD(2102),

	//success
	QUESTION_CREATED(2140),
	QUESTION_FOUND(2141),
	QUESTION_ALL_FOUND(2142),
	QUESTION_UPDATED(2143),
	QUESTION_DELETED(2144);

	private final Integer code;

	@Override
	public Integer getServiceStatus() {
		return code;
	}
}

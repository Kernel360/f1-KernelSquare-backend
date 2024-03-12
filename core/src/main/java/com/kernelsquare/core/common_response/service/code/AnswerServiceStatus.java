package com.kernelsquare.core.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AnswerServiceStatus implements ServiceStatus {
	// error
	ANSWER_UPDATE_NOT_AUTHORIZED(2201),
	ANSWER_NOT_FOUND(2202),
	ANSWER_SELF_IMPOSSIBLE(2203),
	ANSWER_DELETE_NOT_AUTHORIZED(2204),

	// success
	ANSWER_CREATED(2240),
	ANSWERS_ALL_FOUND(2241),
	ANSWER_UPDATED(2242),
	ANSWER_DELETED(2243),
	AUTOMATED_ANSWER_CREATED(2244);

	private final Integer code;

	@Override
	public Integer getServiceStatus() {
		return code;
	}
}

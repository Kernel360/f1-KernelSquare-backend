package com.kernelsquare.core.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AnswerServiceStatus implements ServiceStatus {
	// error
	ANSWER_CREATION_NOT_AUTHORIZED(2200),
	ANSWER_UPDATE_NOT_AUTHORIZED(2201),
	ANSWER_NOT_FOUND(2202),
	ANSWER_SELF_IMPOSSIBLE(2203),

	// success
	ANSWER_CREATED(2240),
	ANSWERS_ALL_FOUND(2241),
	ANSWER_UPDATED(2242),
	ANSWER_DELETED(2243);

	private final Integer code;

	@Override
	public Integer getServiceStatus() {
		return code;
	}
}

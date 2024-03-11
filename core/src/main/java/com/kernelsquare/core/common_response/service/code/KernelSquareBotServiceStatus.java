package com.kernelsquare.core.common_response.service.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KernelSquareBotServiceStatus implements ServiceStatus{
	// error
	ANSWER_ALREADY_EXIST(9200),
	EMPTY_ANSWER_RESPONSE(9201),
	QUESTION_NOT_FOUND(9202),
	KERNEL_SQUARE_BOT_NOT_FOUND(9203);

	private final Integer code;

	@Override
	public Integer getServiceStatus() {
		return code;
	}
}

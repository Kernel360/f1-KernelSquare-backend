package com.kernelsquare.core.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CommonServiceStatus implements ServiceStatus {
	DUPLICATE_DATA_EXIST(9000),
	VALIDATION_CHECK_FAIL(9001),
	INVALID_PARAMETER(9002);

	private final Integer code;

	@Override
	public Integer getServiceStatus() {
		return code;
	}
}

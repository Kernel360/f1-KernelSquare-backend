package com.kernelsquare.core.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TokenServiceStatus implements ServiceStatus {
	INVALID_TOKEN(1300),
	EXPIRED_TOKEN(1301),
	UNAUTHORIZED_TOKEN(1302),
	WRONG_TOKEN(1303),
	EXPIRED_LOGIN_INFO(1304),

	TOKEN_PROCESSING_ERROR(1305);

	private final Integer code;

	@Override
	public Integer getServiceStatus() {
		return code;
	}
}

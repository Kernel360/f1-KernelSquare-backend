package com.kernel360.kernelsquare.global.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AuthServiceStatus implements ServiceStatus {
	INVALID_ACCOUNT(1100),
	INVALID_PASSWORD(1101),
	ALREADY_SAVED_NICKNAME(1102),
	ALREADY_SAVED_EMAIL(1103);

	private final Integer code;

	@Override
	public Integer getServiceStatus() {
		return null;
	}
}

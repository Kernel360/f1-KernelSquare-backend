package com.kernel360.kernelsquare.global.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AuthorityServiceStatus implements ServiceStatus {
	//error
	AUTHORITY_NOT_FOUND(1500);

	private final Integer code;

	@Override
	public Integer getServiceStatus() {
		return code;
	}
}

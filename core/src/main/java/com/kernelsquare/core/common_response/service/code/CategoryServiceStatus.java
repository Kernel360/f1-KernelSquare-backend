package com.kernelsquare.core.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CategoryServiceStatus implements ServiceStatus {
	//error
	CATEGORY_NOT_VALID(2410);

	private final Integer code;

	@Override
	public Integer getServiceStatus() {
		return code;
	}
}

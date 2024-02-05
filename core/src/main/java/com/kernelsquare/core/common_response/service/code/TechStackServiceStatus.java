package com.kernelsquare.core.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TechStackServiceStatus implements ServiceStatus {
	//error
	TECH_STACK_NOT_FOUND(2305),
	TECH_STACK_ALREADY_EXISTED(2306),
	PAGE_NOT_FOUND(2307),

	//sccess
	TECH_STACK_CREATED(2346),
	TECH_STACK_ALL_FOUND(2347),
	TECH_STACK_UPDATED(2348),
	TECH_STACK_DELETED(2349);

	private final Integer code;

	@Override
	public Integer getServiceStatus() {
		return code;
	}
}

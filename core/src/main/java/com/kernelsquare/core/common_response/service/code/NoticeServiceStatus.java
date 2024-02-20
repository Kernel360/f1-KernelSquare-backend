package com.kernelsquare.core.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NoticeServiceStatus implements ServiceStatus {
	NOTICE_NOT_FOUND(4100),
	NOTICE_FOUND(4140),
	NOTICE_ALL_FOUND(4141),
	NOTICE_UPDATED(4142),
	NOTICE_DELETED(4143),
	NOTICE_CREATED(4144);

	private final Integer code;

	@Override
	public Integer getServiceStatus() {
		return code;
	}
}

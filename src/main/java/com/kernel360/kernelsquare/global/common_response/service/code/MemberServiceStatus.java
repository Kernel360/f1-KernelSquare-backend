package com.kernel360.kernelsquare.global.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MemberServiceStatus implements ServiceStatus {
	MEMBER_NOT_FOUND(1201),
	MEMBER_FOUND(1240),
	MEMBER_PASSWORD_UPDATED(1241),
	MEMBER_INFO_UPDATED(1242),
	MEMBER_DELETED(1243);

	private final Integer code;

	@Override
	public Integer getServiceStatus() {
		return code;
	}
}

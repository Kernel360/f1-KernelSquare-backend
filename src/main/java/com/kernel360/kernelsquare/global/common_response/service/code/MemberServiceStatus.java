package com.kernel360.kernelsquare.global.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MemberServiceStatus implements ServiceStatus {
	//error
	MEMBER_NOT_FOUND(1201),

	//success
	MEMBER_FOUND(1240),
	MEMBER_PASSWORD_UPDATED(1241),
	MEMBER_INTRODUCTION_UPDATED(1242),
	MEMBER_PROFILE_UPDATED(1243),
	MEMBER_DELETED(1244);

	private final Integer code;

	@Override
	public Integer getServiceStatus() {
		return code;
	}
}

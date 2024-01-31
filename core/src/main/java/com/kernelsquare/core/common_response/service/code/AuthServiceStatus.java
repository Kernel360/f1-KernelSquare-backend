package com.kernelsquare.core.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AuthServiceStatus implements ServiceStatus {
	//error
	INVALID_ACCOUNT(1100),
	INVALID_PASSWORD(1101),
	ALREADY_SAVED_NICKNAME(1102),
	ALREADY_SAVED_EMAIL(1103),
	UNAUTHORIZED_ACCESS(1104),
	UNAUTHENTICATED(1105),

	//success
	LOGIN_SUCCESS(1140),
	SIGN_UP_SUCCESS(1141),
	EMAIL_UNIQUE_VALIDATED(1142),
	NICKNAME_UNIQUE_VALIDATED(1143),
	ACCESS_TOKEN_REISSUED(1144),
	LOGOUT_SUCCESS(1145);

	private final Integer code;

	@Override
	public Integer getServiceStatus() {
		return code;
	}
}

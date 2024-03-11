package com.kernelsquare.core.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReservationServiceStatus implements ServiceStatus {
	//error
	RESERVATION_NOT_FOUND(3401),
	RESERVATION_LIMIT_EXCEED(3403),
	MEMBER_NOT_FOUND(3404),
	CHAT_ROOM_NOT_FOUND(3405),
	RESERVATION_ARTICLE_NOT_FOUND(3406),
	RESERVATION_ALREADY_EXIST(3407),
	RESERVATION_AVAILABLE_TIME_PASSED(3408),
	DUPLICATE_RESERVATION_TIME(3409),
	RESERVATION_ALREADY_TAKEN(3410),

	//success
	RESERVATION_ALL_FOUND(3440),
	RESERVATION_DELETED(3441),
	RESERVATION_SUCCESS(3442);

	private final Integer code;

	@Override
	public Integer getServiceStatus() {
		return code;
	}
}

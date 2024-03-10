package com.kernelsquare.core.common_response.error.code;

import org.springframework.http.HttpStatus;

import com.kernelsquare.core.common_response.service.code.ReservationServiceStatus;
import com.kernelsquare.core.common_response.service.code.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReservationErrorCode implements ErrorCode {
	RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, ReservationServiceStatus.RESERVATION_NOT_FOUND, "존재하지 않는 예약입니다."),
	RESERVATION_LIMIT_EXCEED(HttpStatus.CONFLICT, ReservationServiceStatus.RESERVATION_LIMIT_EXCEED,
		"예약 가능한 게시글 제한 개수를 넘었습니다."),
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, ReservationServiceStatus.MEMBER_NOT_FOUND,
		"회원이 존재하지 않습니다."),
	RESERVATION_ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, ReservationServiceStatus.RESERVATION_ARTICLE_NOT_FOUND,
		"예약 게시글이 존재하지 않습니다."),
	CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, ReservationServiceStatus.CHAT_ROOM_NOT_FOUND,
		"채팅창이 존재하지 않습니다."),
	RESERVATION_ALREADY_EXIST(HttpStatus.CONFLICT, ReservationServiceStatus.RESERVATION_ALREADY_EXIST,
		"이미 동일한 멘토링을 예약하셨습니다."),
	RESERVATION_AVAILABLE_TIME_PASSED(HttpStatus.BAD_REQUEST,
		ReservationServiceStatus.RESERVATION_AVAILABLE_TIME_PASSED, "예약 가능한 시간이 아닙니다."),
	DUPLICATE_RESERVATION_TIME(HttpStatus.CONFLICT, ReservationServiceStatus.DUPLICATE_RESERVATION_TIME,
		"해당 시간에 이미 다른 멘토링 예약이 존재합니다."),
	RESERVATION_ALREADY_TAKEN(HttpStatus.CONFLICT, ReservationServiceStatus.RESERVATION_ALREADY_TAKEN,
		"선택할 수 없는 예약입니다.");

	private final HttpStatus httpStatus;
	private final ServiceStatus serviceStatus;
	private final String msg;

	@Override
	public HttpStatus getStatus() {
		return httpStatus;
	}

	@Override
	public Integer getCode() {
		return serviceStatus.getServiceStatus();
	}

	@Override
	public String getMsg() {
		return msg;
	}
}

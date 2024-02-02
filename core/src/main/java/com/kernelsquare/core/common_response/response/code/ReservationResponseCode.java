package com.kernelsquare.core.common_response.response.code;

import org.springframework.http.HttpStatus;

import com.kernelsquare.core.common_response.service.code.ReservationServiceStatus;
import com.kernelsquare.core.common_response.service.code.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReservationResponseCode implements ResponseCode {
	RESERVATION_ALL_FOUND(HttpStatus.OK, ReservationServiceStatus.RESERVATION_ALL_FOUND, "예약 전체 조회 성공"),
	RESERVATION_DELETED(HttpStatus.OK, ReservationServiceStatus.RESERVATION_DELETED, "예약 삭제 성공"),
	RESERVATION_SUCCESS(HttpStatus.OK, ReservationServiceStatus.RESERVATION_SUCCESS, "예약 확정");

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

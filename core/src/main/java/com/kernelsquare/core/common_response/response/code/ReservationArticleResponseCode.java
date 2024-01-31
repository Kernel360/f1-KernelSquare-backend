package com.kernelsquare.core.common_response.response.code;

import org.springframework.http.HttpStatus;

import com.kernelsquare.core.common_response.service.code.ReservationArticleServiceStatus;
import com.kernelsquare.core.common_response.service.code.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReservationArticleResponseCode implements ResponseCode {
	RESERVATION_ARTICLE_CREATED(HttpStatus.OK,
		ReservationArticleServiceStatus.RESERVATION_ARTICLE_CREATED, "예약창이 생성되었습니다."),
	RESERVATION_ARTICLE_FOUND(HttpStatus.OK,
		ReservationArticleServiceStatus.RESERVATION_ARTICLE_FOUND, "예약창을 조회했습니다."),
	RESERVATION_ARTICLE_ALL_FOUND(HttpStatus.OK,
		ReservationArticleServiceStatus.RESERVATION_ARTICLE_ALL_FOUND, "모든 예약창을 조회했습니다."),
	RESERVATION_ARTICLE_DELETED(HttpStatus.OK,
		ReservationArticleServiceStatus.RESERVATION_ARTICLE_DELETED, "예약창이 삭제되었습니다."),
	RESERVATION_ARTICLE_UPDATED(HttpStatus.OK,
		ReservationArticleServiceStatus.RESERVATION_ARTICLE_UPDATED, "예약창이 수정되었습니다.");

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


package com.kernelsquare.core.common_response.error.code;

import org.springframework.http.HttpStatus;

import com.kernelsquare.core.common_response.service.code.ReservationArticleServiceStatus;
import com.kernelsquare.core.common_response.service.code.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReservationArticleErrorCode implements ErrorCode {

	RESERVATION_ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, ReservationArticleServiceStatus.RESERVATION_ARTICLE_NOT_FOUND,
		"존재하지 않는 예약창"),
	PAGE_NOT_FOUND(HttpStatus.NOT_FOUND, ReservationArticleServiceStatus.PAGE_NOT_FOUND, "존재하지 않는 페이지"),
	MENTOR_MISMATCH(HttpStatus.BAD_REQUEST, ReservationArticleServiceStatus.MENTOR_MISMATCH, "해당 예약창을 만든 멘토와 불일치"),
	TOO_MANY_HASHTAG(HttpStatus.BAD_REQUEST, ReservationArticleServiceStatus.TOO_MANY_HASHTAG, "최대 해시태그 수를 초과하였습니다."),
	TOO_MANY_RESERVATION(HttpStatus.BAD_REQUEST, ReservationArticleServiceStatus.TOO_MANY_RESERVATION,
		"최대 예약 수를 초과하였습니다."),
	RESERVATION_TIME_LIMIT(HttpStatus.BAD_REQUEST, ReservationArticleServiceStatus.RESERVATION_TIME_LIMIT,
		"최대 예약 기간을 초과하였습니다."),
	STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, ReservationArticleServiceStatus.STATUS_NOT_FOUND, "존재하지 않는 상태값입니다."),
	RESERVATION_PERIOD_LIMIT(HttpStatus.BAD_REQUEST, ReservationArticleServiceStatus.RESERVATION_PERIOD_LIMIT,
		"가능한 예약 기간이 아닙니다."),
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, ReservationArticleServiceStatus.MEMBER_NOT_FOUND, "존재하지 않은 회원"),
	DELETE_ONLY_BEFORE_7DAYS(HttpStatus.BAD_REQUEST, ReservationArticleServiceStatus.DELETE_ONLY_BEFORE_7DAYS,
		"7일 이전에만 삭제 가능합니다."),
	TOO_MANY_RESERVATION_ARTICLE(HttpStatus.BAD_REQUEST, ReservationArticleServiceStatus.TOO_MANY_RESERVATION_ARTICLE,
			"이미 예약창이 있습니다.");

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


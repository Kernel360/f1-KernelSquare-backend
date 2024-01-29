package com.kernel360.kernelsquare.global.common_response.error.code;


import com.kernel360.kernelsquare.global.common_response.service.code.ReservationArticleServiceStatus;
import com.kernel360.kernelsquare.global.common_response.service.code.ServiceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ReservationArticleErrorCode implements ErrorCode {

    RESERVATION_ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, ReservationArticleServiceStatus.RESERVATION_ARTICLE_NOT_FOUND, "존재하지 않는 예약창"),
    PAGE_NOT_FOUND(HttpStatus.NOT_FOUND, ReservationArticleServiceStatus.PAGE_NOT_FOUND, "존재하지 않는 페이지"),
    MENTOR_MISMATCH(HttpStatus.BAD_REQUEST, ReservationArticleServiceStatus.MENTOR_MISMATCH, "해당 예약창을 만든 멘토와 불일치"),
    TOO_MANY_HASHTAG(HttpStatus.BAD_REQUEST, ReservationArticleServiceStatus.TOO_MANY_HASHTAG, "최대 해시태그 수를 초과하였습니다."),
    TOO_MANY_RESERVATION(HttpStatus.BAD_REQUEST, ReservationArticleServiceStatus.TOO_MANY_RESERVATION, "최대 예약 수를 초과하였습니다."),
    RESERVATION_TIME_LIMIT(HttpStatus.BAD_REQUEST, ReservationArticleServiceStatus.RESERVATION_TIME_LIMIT, "최대 예약 기간을 초과하였습니다.");


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

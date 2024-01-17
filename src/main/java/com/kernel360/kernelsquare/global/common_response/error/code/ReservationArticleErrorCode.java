package com.kernel360.kernelsquare.global.common_response.error.code;


import com.kernel360.kernelsquare.global.common_response.service.code.ReservationArticleServiceStatus;
import com.kernel360.kernelsquare.global.common_response.service.code.ServiceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ReservationArticleErrorCode implements ErrorCode {

    RESERVATION_ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, ReservationArticleServiceStatus.RESERVATION_ARTICLE_NOT_FOUND, "존재하지 않는 예약창"),
    PAGE_NOT_FOUND(HttpStatus.NOT_FOUND, ReservationArticleServiceStatus.PAGE_NOT_FOUND, "존재하지 않는 페이지");


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

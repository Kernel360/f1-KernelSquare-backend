package com.kernel360.kernelsquare.global.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReservationArticleServiceStatus implements ServiceStatus {
    //error


    //success
    RESERVATION_ARTICLE_CREATED(3140);


    private final Integer code;

    @Override
    public Integer getServiceStatus() { return code;}

}

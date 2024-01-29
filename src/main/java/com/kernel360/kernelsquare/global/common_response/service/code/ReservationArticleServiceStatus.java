package com.kernel360.kernelsquare.global.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReservationArticleServiceStatus implements ServiceStatus {
    //error
    RESERVATION_ARTICLE_NOT_FOUND(3100),
    PAGE_NOT_FOUND(3101),
    MENTOR_MISMATCH(3102),
    TOO_MANY_HASHTAG(3103),
    TOO_MANY_RESERVATION(3104),
    RESERVATION_TIME_LIMIT(3105),


    //success
    RESERVATION_ARTICLE_CREATED(3140),
    RESERVATION_ARTICLE_FOUND(3141),
    RESERVATION_ARTICLE_ALL_FOUND(3142),
    RESERVATION_ARTICLE_DELETED(3143),
    RESERVATION_ARTICLE_UPDATED(3144);



    private final Integer code;

    @Override
    public Integer getServiceStatus() { return code;}

}

package com.kernel360.kernelsquare.domain.reservation_article.dto;

import com.kernel360.kernelsquare.domain.hashtag.dto.UpdateHashtagRequest;
import com.kernel360.kernelsquare.domain.reservation.dto.UpdateReservationRequest;

import java.io.Serializable;
import java.util.List;

public record UpdateReservationArticleRequest(
        Long memberId,
        Long articleId,
        String title,
        String content,
        List<UpdateHashtagRequest> changeHashtags,
        List<UpdateReservationRequest> changeReservations
) { }
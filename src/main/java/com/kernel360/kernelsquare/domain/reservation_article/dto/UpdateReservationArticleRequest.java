package com.kernel360.kernelsquare.domain.reservation_article.dto;

import com.kernel360.kernelsquare.domain.hashtag.dto.UpdateHashtagRequest;
import com.kernel360.kernelsquare.domain.reservation.dto.UpdateReservationRequest;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;

public record UpdateReservationArticleRequest(
        Long memberId,
        Long articleId,
        @NotBlank(message = "제목을 입력해 주세요.")
        String title,
        @NotBlank(message = "내용을 입력해 주세요.")
        String content,
        List<UpdateHashtagRequest> changeHashtags,
        List<UpdateReservationRequest> changeReservations
) { }
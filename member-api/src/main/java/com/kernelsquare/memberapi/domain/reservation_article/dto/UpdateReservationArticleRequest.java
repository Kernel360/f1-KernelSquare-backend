package com.kernelsquare.memberapi.domain.reservation_article.dto;

import java.util.List;

import com.kernelsquare.memberapi.domain.hashtag.dto.UpdateHashtagRequest;
import com.kernelsquare.memberapi.domain.reservation.dto.UpdateReservationRequest;

import jakarta.validation.constraints.NotBlank;

public record UpdateReservationArticleRequest(
	Long memberId,
	Long articleId,
	@NotBlank(message = "제목을 입력해 주세요.")
	String title,
	@NotBlank(message = "내용을 입력해 주세요.")
	String content,
	List<UpdateHashtagRequest> changeHashtags,
	List<UpdateReservationRequest> changeReservations
) {
}
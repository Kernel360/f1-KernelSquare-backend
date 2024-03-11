package com.kernelsquare.memberapi.domain.reservation_article.dto;

import java.util.List;

import com.kernelsquare.core.validation.annotations.BadWordFilter;
import com.kernelsquare.core.validation.constants.BadWordValidationMessage;
import com.kernelsquare.memberapi.domain.hashtag.dto.UpdateHashtagRequest;
import com.kernelsquare.memberapi.domain.reservation.dto.UpdateReservationRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateReservationArticleRequest(
	@NotNull(message = "회원 ID를 입력해 주세요.")
	Long memberId,
	@NotNull(message = "게시글 ID를 입력해 주세요.")
	Long articleId,
	@NotBlank(message = "예약창 제목을 입력해 주세요.")
	@Size(min = 5, max = 100, message = "예약창 제목은 5자 이상 100자 이하로 작성해 주세요.")
	@BadWordFilter(message = BadWordValidationMessage.NO_BAD_WORD_IN_CONTENT)
	String title,
	@NotBlank(message = "예약창 내용을 입력해 주세요.")
	@Size(min = 10, max = 1000, message = "예약창 내용은 10자 이상 1000자 이하로 작성해 주세요.")
	@BadWordFilter(message = BadWordValidationMessage.NO_BAD_WORD_IN_CONTENT)
	String content,
	@NotBlank(message = "예약 게시글 소개를 입력해 주세요.")
	@Size(min = 10, max = 150, message = "예약 게시글 소개는 10자 이상 150자 이하로 작성해 주세요.")
	@BadWordFilter(message = BadWordValidationMessage.NO_BAD_WORD_IN_CONTENT)
	String introduction,
	List<UpdateHashtagRequest> changeHashtags,
	List<UpdateReservationRequest> changeReservations
) {
}
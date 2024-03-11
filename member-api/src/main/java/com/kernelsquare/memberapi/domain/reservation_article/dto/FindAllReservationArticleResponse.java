package com.kernelsquare.memberapi.domain.reservation_article.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kernelsquare.core.constants.TimeResponseFormat;
import com.kernelsquare.domainmysql.domain.hashtag.entity.Hashtag;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.reservation_article.entity.ReservationArticle;
import com.kernelsquare.core.util.ImageUtils;

import lombok.Builder;

@Builder
public record FindAllReservationArticleResponse(
	Long articleId,
	Long memberId,
	String nickname,
	String memberImageUrl,
	Long level,
	String levelImageUrl,
	String title,
	String introduction,
	List<String> hashTagList,
	Long coffeeChatCount,
	Long availableReservationCount,
	Long totalReservationCount,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeResponseFormat.PATTERN)
	LocalDateTime createdDate,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeResponseFormat.PATTERN)
	LocalDateTime modifiedDate,
	Boolean articleStatus
) {
	public static FindAllReservationArticleResponse of(
		Member member,
		ReservationArticle article,
		Boolean articleStatus,
		Long coffeeChatCount,
		Long availableReservationCount,
		Long totalReservationCount
	) {
		return FindAllReservationArticleResponse
			.builder()
			.articleId(article.getId())
			.memberId(member.getId())
			.nickname(member.getNickname())
			.memberImageUrl(ImageUtils.makeImageUrl(member.getImageUrl()))
			.level(member.getLevel().getName())
			.levelImageUrl(ImageUtils.makeImageUrl(member.getLevel().getImageUrl()))
			.title(article.getTitle())
			.introduction(article.getIntroduction())
			.hashTagList(article.getHashtagList().stream().map(Hashtag::getContent).toList())
			.coffeeChatCount(coffeeChatCount)
			.availableReservationCount(availableReservationCount)
			.totalReservationCount(totalReservationCount)
			.createdDate(article.getCreatedDate())
			.modifiedDate(article.getModifiedDate())
			.articleStatus(articleStatus)
			.build();
	}
}
package com.kernelsquare.memberapi.domain.reservation_article.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kernelsquare.core.constants.TimeResponseFormat;
import com.kernelsquare.domainmysql.domain.hashtag.entity.Hashtag;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.reservation_article.entity.ReservationArticle;
import com.kernelsquare.core.util.ImageUtils;

public record FindAllReservationArticleResponse(
	Long articleId,
	Long memberId,
	String nickname,
	String memberImageUrl,
	Long level,
	String levelImageUrl,
	String title,
	List<String> hashTagList,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeResponseFormat.PATTERN)
	LocalDateTime createdDate,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeResponseFormat.PATTERN)
	LocalDateTime modifiedDate,
	Boolean articleStatus,
	Long fullCheck
) {
	public static FindAllReservationArticleResponse of(
		Member member,
		ReservationArticle article,
		Boolean articleStatus,
		Long fullCheck) {
		return new FindAllReservationArticleResponse(
			article.getId(),
			member.getId(),
			member.getNickname(),
			ImageUtils.makeImageUrl(member.getImageUrl()),
			member.getLevel().getName(),
			ImageUtils.makeImageUrl(member.getLevel().getImageUrl()),
			article.getTitle(),
			article.getHashtagList().stream().map(Hashtag::getContent).toList(),
			article.getCreatedDate(),
			article.getModifiedDate(),
			articleStatus,
			fullCheck
		);
	}
}
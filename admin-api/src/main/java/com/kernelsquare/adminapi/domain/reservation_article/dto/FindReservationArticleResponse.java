package com.kernelsquare.adminapi.domain.reservation_article.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.kernelsquare.core.util.ImageUtils;
import com.kernelsquare.adminapi.domain.reservation.dto.FindReservationResponse;
import com.kernelsquare.domainmysql.domain.hashtag.entity.Hashtag;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.reservation_article.entity.ReservationArticle;

public record FindReservationArticleResponse(
	Long articleId,
	Long memberId,
	String nickname,
	String memberImageUrl,
	Long level,
	String levelImageUrl,
	String title,
	String content,
	List<String> hashTagList,
	List<FindReservationResponse> dataTimes,
	LocalDateTime createdDate,
	LocalDateTime modifiedDate
) {
	public static FindReservationArticleResponse of(
		Member member,
		ReservationArticle article,
		List<FindReservationResponse> reservationDtos,
		Level level) {
		return new FindReservationArticleResponse(
			article.getId(),
			member.getId(),
			member.getNickname(),
			ImageUtils.makeImageUrl(member.getImageUrl()),
			level.getName(),
			ImageUtils.makeImageUrl(level.getImageUrl()),
			article.getTitle(),
			article.getContent(),
			article.getHashtagList().stream().map(Hashtag::getContent).toList(),
			reservationDtos,
			article.getCreatedDate(),
			article.getModifiedDate()
		);

	}
}

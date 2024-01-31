package com.kernelsquare.memberapi.domain.hashtag.dto;

import com.kernelsquare.domainmysql.domain.hashtag.entity.Hashtag;

import lombok.Builder;

@Builder
public record FindHashtagResponse(
	Long hashtagId,
	String content
) {
	public static FindHashtagResponse from(Hashtag hashtag) {
		return new FindHashtagResponse(
			hashtag.getId(),
			hashtag.getContent()
		);
	}
}
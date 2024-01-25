package com.kernel360.kernelsquare.domain.hashtag.dto;

import com.kernel360.kernelsquare.domain.hashtag.entity.Hashtag;
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
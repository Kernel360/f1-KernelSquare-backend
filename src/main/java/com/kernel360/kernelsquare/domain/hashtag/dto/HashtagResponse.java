package com.kernel360.kernelsquare.domain.hashtag.dto;

import com.kernel360.kernelsquare.domain.hashtag.entity.Hashtag;

/**
 * DTO for {@link com.kernel360.kernelsquare.domain.hashtag.entity.Hashtag}
 */
public record HashtagResponse(
        Long id,
        String content
) {
    public static HashtagResponse from(Hashtag hashtag) {
        return new HashtagResponse(
                hashtag.getId(),
                hashtag.getContent()
        );
    }
}
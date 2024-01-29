package com.kernel360.kernelsquare.domain.hashtag.dto;

import com.kernel360.kernelsquare.domain.hashtag.entity.Hashtag;

/**
 * DTO for {@link com.kernel360.kernelsquare.domain.hashtag.entity.Hashtag}
 */
public record FindHashtagResponse(
        Long id,
        String content
) {
    public static FindHashtagResponse from(Hashtag hashtag) {
        return new FindHashtagResponse(
                hashtag.getId(),
                hashtag.getContent()
        );
    }
}
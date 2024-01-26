package com.kernel360.kernelsquare.domain.hashtag.dto;

import java.util.List;

public record FindAllHashtagResponse(
        List<FindHashtagResponse> hashtags
) {
    public static FindAllHashtagResponse from(List<FindHashtagResponse> hashtags) {
        return new FindAllHashtagResponse(hashtags);
    }
}
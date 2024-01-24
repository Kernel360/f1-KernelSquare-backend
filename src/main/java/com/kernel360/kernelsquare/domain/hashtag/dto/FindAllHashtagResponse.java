package com.kernel360.kernelsquare.domain.hashtag.dto;

import com.kernel360.kernelsquare.domain.hashtag.entity.Hashtag;

import java.util.List;

public record FindAllHashtagResponse(
        List<HashtagResponse> hashtags
) {
    public static FindAllHashtagResponse from(List<HashtagResponse> hashtags) {
        return new FindAllHashtagResponse(hashtags);
    }
}
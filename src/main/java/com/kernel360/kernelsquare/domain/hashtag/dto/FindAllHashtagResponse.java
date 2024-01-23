package com.kernel360.kernelsquare.domain.hashtag.dto;

import com.kernel360.kernelsquare.domain.hashtag.entity.Hashtag;

import java.util.List;

public record FindAllHashtagResponse(
        List<Hashtag> hashtags
) {
    public static FindAllHashtagResponse from(List<Hashtag> hashtags) {
        return new FindAllHashtagResponse(hashtags);
    }
}
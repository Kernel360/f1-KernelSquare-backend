package com.kernel360.kernelsquare.domain.hashtag.controller;

import com.kernel360.kernelsquare.domain.hashtag.service.HashtagService;
import com.kernel360.kernelsquare.domain.hashtag.dto.FindAllHashtagResponse;
import com.kernel360.kernelsquare.global.common_response.ApiResponse;
import com.kernel360.kernelsquare.global.common_response.ResponseEntityFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.kernel360.kernelsquare.global.common_response.response.code.HashTagResponseCode.HASHTAG_ALL_FOUND;
import static com.kernel360.kernelsquare.global.common_response.response.code.HashTagResponseCode.HASHTAG_DELETED;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class HashtagController {
    private final HashtagService hashtagService;

    @GetMapping("/hashtags")
    public ResponseEntity<ApiResponse<FindAllHashtagResponse>> findAllHashtag() {
        FindAllHashtagResponse response = hashtagService.findAllHashtag();

        return ResponseEntityFactory.toResponseEntity(HASHTAG_ALL_FOUND, response);
    }

    @DeleteMapping("/hashtags/{hashtagId}")
    public ResponseEntity<ApiResponse> deleteHashtag(
            @PathVariable
            Long hashtagId
    ) {
        hashtagService.deleteHashtag(hashtagId);

        return ResponseEntityFactory.toResponseEntity(HASHTAG_DELETED);
    }
}

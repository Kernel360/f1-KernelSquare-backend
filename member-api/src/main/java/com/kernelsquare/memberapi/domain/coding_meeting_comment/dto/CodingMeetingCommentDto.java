package com.kernelsquare.memberapi.domain.coding_meeting_comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

public class CodingMeetingCommentDto {

    @Builder
    public record FindAllResponse(
            Long memberId,
            Long memberLevel,
            String memberNickname,
            String memberProfileUrl,
            String memberLevelImageUrl,
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
            LocalDateTime createdDate,
            String codingMeetingCommentToken,
            String codingMeetingCommentContent
    ) {
    }

    @Builder
    public record CreateRequest(
       Long memberId,
       String codingMeetingToken,
       String codingMeetingCommentContent
    ) {
    }

    @Builder
    public record UpdateRequest(
       String codingMeetingCommentContent
    ) {
    }
}

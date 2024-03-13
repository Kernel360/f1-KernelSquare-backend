package com.kernelsquare.memberapi.domain.coding_meeting_comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kernelsquare.core.constants.TimeResponseFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeResponseFormat.PATTERN)
            LocalDateTime createdDate,
            String codingMeetingCommentToken,
            String codingMeetingCommentContent
    ) {
    }

    @Builder
    public record CreateRequest(
       String codingMeetingToken,
       @NotBlank(message = "모각코 댓글 내용은 필수 입력사항입니다.")
       @Size(max = 300, message = "모각코 댓글 내용은 300자 이하로 작성이 가능합니다.")
       String codingMeetingCommentContent
    ) {
    }

    @Builder
    public record UpdateRequest(
       @NotBlank(message = "모각코 댓글 내용은 필수 입력사항입니다.")
       @Size(max = 300, message = "모각코 댓글 내용은 300자 이하로 작성이 가능합니다.")
       String codingMeetingCommentContent
    ) {
    }
}

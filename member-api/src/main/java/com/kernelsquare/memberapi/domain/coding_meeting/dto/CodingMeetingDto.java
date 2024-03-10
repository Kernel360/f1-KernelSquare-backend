package com.kernelsquare.memberapi.domain.coding_meeting.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kernelsquare.core.constants.TimeResponseFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public class CodingMeetingDto {

    @Builder
    public record CreateRequest(
            @NotBlank(message = "모각코 제목은 필수 입력사항입니다.")
            @Size(min = 5, max = 100, message = "모각코 모집글 제목은 5자 이상 100자 이하로 작성이 가능합니다.")
            String codingMeetingTitle,
            @NotBlank(message = "모각코 위치 ID는 필수 입력사항입니다.")
            String codingMeetingLocationId,
            @NotBlank(message = "모각코 위치 명은 필수 입력사항입니다.")
            String codingMeetingLocationPlaceName,
            @NotBlank(message = "모각코 위치 경도는 필수 입력사항입니다.")
            String codingMeetingLocationLongitude,
            @NotBlank(message = "모각코 위치 위도는 필수 입력사항입니다.")
            String codingMeetingLocationLatitude,
            @Min(value = 3, message = "모집 인원 상한은 최소 3인입니다.")
            @Max(value = 6, message = "모집 인원 상한은 최대 6인입니다.")
            Long codingMeetingMemberUpperLimit,
            LocalDateTime codingMeetingStartTime,
            LocalDateTime codingMeetingEndTime,
            List<String> codingMeetingHashtags,
            @NotBlank(message = "모각코 게시글 내용은 필수 입력사항입니다.")
            @Size(min = 10, max = 10000, message = "모각코 모집글 내용은 10자 이상 10000자 이하로 작성이 가능합니다.")
            String codingMeetingContent
            ) {
    }

    @Builder
    public record CreateResponse(
            String codingMeetingToken
    ) {
    }

    @Builder
    public record UpdateRequest(
            @NotBlank(message = "모각코 제목은 필수 입력사항입니다.")
            @Size(min = 5, max = 100, message = "모각코 모집글 제목은 5자 이상 100자 이하로 작성이 가능합니다.")
            String codingMeetingTitle,
            @NotBlank(message = "모각코 위치 ID는 필수 입력사항입니다.")
            String codingMeetingLocationId,
            @NotBlank(message = "모각코 위치 명은 필수 입력사항입니다.")
            String codingMeetingLocationPlaceName,
            @NotBlank(message = "모각코 위치 경도는 필수 입력사항입니다.")
            String codingMeetingLocationLongitude,
            @NotBlank(message = "모각코 위치 위도는 필수 입력사항입니다.")
            String codingMeetingLocationLatitude,
            @Min(value = 3, message = "모집 인원 상한은 최소 3인입니다.")
            @Max(value = 6, message = "모집 인원 상한은 최대 6인입니다.")
            Long codingMeetingMemberUpperLimit,
            LocalDateTime codingMeetingStartTime,
            LocalDateTime codingMeetingEndTime,
            List<String> codingMeetingHashtags,
            @NotBlank(message = "모각코 게시글 내용은 필수 입력사항입니다.")
            @Size(min = 10, max = 10000, message = "모각코 모집글 내용은 10자 이상 10000자 이하로 작성이 가능합니다.")
            String codingMeetingContent
    ) {
    }
    @Builder
    public record FindResponse(
            Long memberId,
            Long memberLevel,
            String memberNickname,
            String memberProfileUrl,
            String memberLevelImageUrl,

            String codingMeetingTitle,
            String codingMeetingToken,
            String codingMeetingContent,
            List<String> codingMeetingHashtags,

            String codingMeetingLocationId,
            String codingMeetingLocationPlaceName,
            String codingMeetingLocationLongitude,
            String codingMeetingLocationLatitude,

            Long codingMeetingMemberUpperLimit,
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeResponseFormat.PATTERN)
            LocalDateTime codingMeetingStartTime,
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeResponseFormat.PATTERN)
            LocalDateTime codingMeetingEndTime,
            Boolean codingMeetingClosed
    ) {
    }

    @Builder
    public record FindAllResponse(
            Long memberId,
            Long memberLevel,
            String memberNickname,
            String memberProfileUrl,
            String memberLevelImageUrl,

            String codingMeetingTitle,
            String codingMeetingToken,
            List<String>codingMeetingHashtags,
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeResponseFormat.PATTERN)
            LocalDateTime codingMeetingStartTime,
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeResponseFormat.PATTERN)
            LocalDateTime codingMeetingEndTime,
            Boolean codingMeetingClosed,
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeResponseFormat.PATTERN)
            LocalDateTime createdDate
    ) {
    }
}

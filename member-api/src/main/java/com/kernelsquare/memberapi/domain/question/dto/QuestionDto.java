package com.kernelsquare.memberapi.domain.question.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kernelsquare.core.constants.TimeResponseFormat;
import com.kernelsquare.core.util.ImageUtils;
import com.kernelsquare.domainmysql.domain.question.dto.FindAllQuestions;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class QuestionDto {
    @Builder
    public record FindAllRequest(
        Long totalPage,
        Long pageable
    ) {}

    @Builder
    public record FindAllResponse(
        Long id,
        String title,
        String questionImageUrl,
        Long viewCount,
        Boolean closeStatus,
        Long memberId,
        String nickname,
        String memberImageUrl,
        Long level,
        String levelImageUrl,
        List<String> skills,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeResponseFormat.PATTERN)
        LocalDateTime createdDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TimeResponseFormat.PATTERN)
        LocalDateTime modifiedDate
    ) {
        public static FindAllResponse of(FindAllQuestions findAllQuestions) {
            return FindAllResponse.builder()
                .id(findAllQuestions.getId())
                .title(findAllQuestions.getTitle())
                .questionImageUrl(ImageUtils.makeImageUrl(findAllQuestions.getImageUrl()))
                .viewCount(findAllQuestions.getViewCount())
                .closeStatus(findAllQuestions.getClosedStatus())
                .memberId(findAllQuestions.getMemberId())
                .nickname(findAllQuestions.getNickname())
                .memberImageUrl(ImageUtils.makeImageUrl(findAllQuestions.getMemberImageUrl()))
                .level(findAllQuestions.getLevel())
                .levelImageUrl(ImageUtils.makeImageUrl(findAllQuestions.getLevelImageUrl()))
                .skills(Objects.isNull(findAllQuestions.getSkills()) ? List.of() : List.of(findAllQuestions.getSkills().split(",")))
                .createdDate(findAllQuestions.getCreatedDate())
                .modifiedDate(findAllQuestions.getModifiedDate())
                .build();
        }
    }
}

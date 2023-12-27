package com.kernel360.kernelsquare.domain.question.dto;

import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.question.entity.Question;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateQuestionRequest(

    @NotNull
    Long memberId,
    @NotBlank
    String title,
    @NotBlank
    String content,
    String imageUrl,
    @NotNull
    List<String> skills
) {
    public static Question toEntity(CreateQuestionRequest createQuestionRequest, Member member) {
        return Question.builder()
            .title(createQuestionRequest.title())
            .content(createQuestionRequest.content())
            .imageUrl(createQuestionRequest.imageUrl())
            .viewCount(0L)
            .closedStatus(false)
            .member(member)
            .build();
    }
}

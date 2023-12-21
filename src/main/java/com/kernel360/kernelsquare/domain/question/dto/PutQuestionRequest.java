package com.kernel360.kernelsquare.domain.question.dto;

import com.kernel360.kernelsquare.domain.question.entity.Question;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PutQuestionRequest(
    @NotBlank
    String title,
    @NotBlank
    String content,
    String imageUrl,
    @NotNull
    List<String> skills
) {
    public static Question toEntity(PutQuestionRequest putQuestionRequest) {
        return Question.builder()
            .title(putQuestionRequest.title())
            .content(putQuestionRequest.content())
            .imageUrl(putQuestionRequest.imageUrl())
            .build();
    }
}

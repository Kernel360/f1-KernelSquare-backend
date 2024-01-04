package com.kernel360.kernelsquare.domain.question.dto;

import com.kernel360.kernelsquare.domain.image.utils.ImageUtils;
import com.kernel360.kernelsquare.domain.question.entity.Question;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateQuestionRequest(
    @NotBlank
    String title,
    @NotBlank
    String content,
    String imageUrl,
    @NotNull
    List<String> skills
) {
//    public static Question toEntity(UpdateQuestionRequest updateQuestionRequest) {
//        return Question.builder()
//            .title(updateQuestionRequest.title())
//            .content(updateQuestionRequest.content())
//            .imageUrl(updateQuestionRequest.imageUrl()!=null? ImageUtils.parseFilePath(updateQuestionRequest.imageUrl()): null)
//            .build();
//    }
}

package com.kernel360.kernelsquare.domain.image.dto;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public enum ImageCategoryDto {
    MEMBER("member"),
    QUESTION("question"),
    ANSWER("answer"),
    LEVEL("level"),
    RANK("rank");

    private final String category;

    public String getCategory() {
        return category;
    }

    public static List<String> getCategoryList() {
        return Arrays.stream(ImageCategoryDto.values()).map(ImageCategoryDto::getCategory).toList();
    }
}

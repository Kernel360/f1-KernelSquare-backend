package com.kernel360.kernelsquare.domain.image.dto;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public enum CategoryDto {
    MEMBER("member"),
    QUESTION("question"),
    ANSWER("answer");

    private final String category;

    public String getCategory() {
        return category;
    }

    public static List<String> getCategoryList() {
        return Arrays.stream(CategoryDto.values()).map(CategoryDto::getCategory).toList();
    }
}

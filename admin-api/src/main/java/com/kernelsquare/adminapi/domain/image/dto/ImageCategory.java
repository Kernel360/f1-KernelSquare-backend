package com.kernelsquare.adminapi.domain.image.dto;

import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ImageCategory {
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
        return Arrays.stream(ImageCategory.values()).map(ImageCategory::getCategory).toList();
    }
}

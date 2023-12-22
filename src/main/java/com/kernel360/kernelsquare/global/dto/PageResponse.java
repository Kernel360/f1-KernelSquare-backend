package com.kernel360.kernelsquare.global.dto;

import java.util.List;

public record PageResponse<T>(
    Pagination pagination,
    List<T> list
) {
    public static <T> PageResponse<T> of(
        Pagination pagination,
        List<T> list
    ) {
        return new PageResponse<>(pagination, list);
    }
}

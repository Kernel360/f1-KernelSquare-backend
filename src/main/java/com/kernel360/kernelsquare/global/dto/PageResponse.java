package com.kernel360.kernelsquare.global.dto;

import java.util.List;

public record PageResponse<D>(
    Pagination pagination,
    List<D> list
) {
    public static <D> PageResponse<D> of(
        Pagination pagination,
        List<D> list
    ) {
        return new PageResponse<>(pagination, list);
    }
}

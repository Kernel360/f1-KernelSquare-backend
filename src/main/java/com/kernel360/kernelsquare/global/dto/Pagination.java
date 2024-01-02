package com.kernel360.kernelsquare.global.dto;

import lombok.Builder;

public record Pagination(
    Integer totalPage,
    Integer pageable,
    Boolean isEnd
) {
    @Builder
    public Pagination(Integer totalPage, Integer pageable, Boolean isEnd) {
        this.totalPage = totalPage;
        this.pageable = pageable;
        this.isEnd = isEnd;
    }

    public static Pagination toEntity(Integer totalPage, Integer pageable, Boolean isEnd) {
        return Pagination.builder()
            .totalPage(totalPage)
            .pageable(pageable)
            .isEnd(isEnd)
            .build();
    }
}

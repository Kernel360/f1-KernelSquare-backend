package com.kernelsquare.memberapi.domain.search.dto;

import com.kernelsquare.core.dto.PageResponse;
import com.kernelsquare.core.dto.Pagination;

import java.util.List;

public record SearchTechStackResponse(
    Long totalCount,
    Pagination pagination,
    List<String> techStackList
) {
    public static SearchTechStackResponse of(Long totalCount, PageResponse<String> pageResponse) {
        return new SearchTechStackResponse(totalCount, pageResponse.pagination(), pageResponse.list());
    }
}

package com.kernel360.kernelsquare.domain.search.dto;

import com.kernel360.kernelsquare.domain.question.dto.FindQuestionResponse;
import com.kernel360.kernelsquare.global.dto.PageResponse;
import com.kernel360.kernelsquare.global.dto.Pagination;

import java.util.List;

public record SearchQuestionResponse(
    Long totalCount,
    Pagination pagination,
    List<FindQuestionResponse> questionList
) {
    public static SearchQuestionResponse of(Long totalCount, PageResponse<FindQuestionResponse> pageResponse) {
        return new SearchQuestionResponse(totalCount, pageResponse.pagination(), pageResponse.list());
    }
}

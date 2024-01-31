package com.kernelsquare.adminapi.domain.search.dto;

import java.util.List;

import com.kernelsquare.adminapi.domain.question.dto.FindQuestionResponse;
import com.kernelsquare.core.dto.PageResponse;
import com.kernelsquare.core.dto.Pagination;

public record SearchQuestionResponse(
	Long totalCount,
	Pagination pagination,
	List<FindQuestionResponse> questionList
) {
	public static SearchQuestionResponse of(Long totalCount, PageResponse<FindQuestionResponse> pageResponse) {
		return new SearchQuestionResponse(totalCount, pageResponse.pagination(), pageResponse.list());
	}
}

package com.kernelsquare.adminapi.domain.search.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kernelsquare.adminapi.domain.question.dto.FindQuestionResponse;
import com.kernelsquare.adminapi.domain.search.dto.SearchQuestionResponse;
import com.kernelsquare.core.common_response.error.code.QuestionErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.core.dto.PageResponse;
import com.kernelsquare.core.dto.Pagination;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import com.kernelsquare.domainmysql.domain.search.repository.SearchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {
	private final SearchRepository searchRepository;

	public SearchQuestionResponse searchQuestions(Pageable pageable, String keyword) {

		Integer currentPage = pageable.getPageNumber() + 1;

		Page<Question> pages = searchRepository.searchQuestionsByKeyword(pageable, keyword);

		Long totalCount = pages.getTotalElements();

		Integer totalPages = pages.getTotalPages();

		if (totalPages == 0)
			totalPages += 1;

		if (currentPage > totalPages) {
			throw new BusinessException(QuestionErrorCode.PAGE_NOT_FOUND);
		}

		Pagination pagination = Pagination.toEntity(totalPages, pages.getSize(), currentPage.equals(totalPages));

		List<FindQuestionResponse> pageQuestionList = pages.getContent().stream()
			.map(question -> FindQuestionResponse.of(question.getMember(), question, question.getMember().getLevel()))
			.toList();

		PageResponse<FindQuestionResponse> pageResponse = PageResponse.of(pagination, pageQuestionList);

		return SearchQuestionResponse.of(totalCount, pageResponse);
	}
}

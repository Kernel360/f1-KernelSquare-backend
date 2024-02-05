package com.kernelsquare.memberapi.domain.tech_stack.service;

import com.kernelsquare.core.common_response.error.code.TechStackErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.core.dto.PageResponse;
import com.kernelsquare.core.dto.Pagination;
import com.kernelsquare.domainmysql.domain.tech_stack.entity.TechStack;
import com.kernelsquare.domainmysql.domain.tech_stack.repository.TechStackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TechStackService {
	private final TechStackRepository techStackRepository;

	@Transactional(readOnly = true)
	public PageResponse<String> findAllTechStacks(Pageable pageable) {
		Integer currentPage = pageable.getPageNumber() + 1;

		Page<TechStack> pages = techStackRepository.findAll(pageable);

		Integer totalPages = pages.getTotalPages();

		if (totalPages == 0)
			totalPages += 1;

		if (currentPage > totalPages) {
			throw new BusinessException(TechStackErrorCode.PAGE_NOT_FOUND);
		}

		Pagination pagination = Pagination.toEntity(totalPages, pages.getSize(), currentPage.equals(totalPages));

		List<String> responsePages = pages.getContent().stream()
			.map(TechStack::getSkill)
			.toList();

		return PageResponse.of(pagination, responsePages);
	}
}

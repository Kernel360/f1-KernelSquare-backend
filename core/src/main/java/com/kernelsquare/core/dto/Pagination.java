package com.kernelsquare.core.dto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

	public static Pagination of(Pageable pageable, Page page) {
		return Pagination.builder()
			.totalPage(page.getTotalPages())
			.pageable(page.getSize())
			.isEnd((pageable.getPageNumber() + 1) == page.getTotalPages())
			.build();
	}
}

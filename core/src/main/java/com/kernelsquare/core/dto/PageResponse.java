package com.kernelsquare.core.dto;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.Builder;

@Builder
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

	public static <T> PageResponse of(Pageable pageable, Page page, List list) {
		return PageResponse.builder()
			.pagination(Pagination.of(pageable, page))
			.list(list)
			.build();
	}
}

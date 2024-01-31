package com.kernelsquare.core.dto;

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

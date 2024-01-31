package com.kernelsquare.core.common_response;

import com.fasterxml.jackson.annotation.JsonInclude;

public record ApiResponse<T>(
	Integer code,
	String msg,
	@JsonInclude(JsonInclude.Include.NON_NULL)
	T data
) {
	public static <T> ApiResponse<T> of(
		StatusCode statusCode,
		T data) {
		return new ApiResponse(
			statusCode.getCode(),
			statusCode.getMsg(),
			data
		);
	}

	public static <T> ApiResponse<T> of(
		StatusCode statusCode
	) {
		return new ApiResponse<>(
			statusCode.getCode(),
			statusCode.getMsg(),
			null
		);
	}
}

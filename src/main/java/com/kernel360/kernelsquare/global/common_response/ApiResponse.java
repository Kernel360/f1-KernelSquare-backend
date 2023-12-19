package com.kernel360.kernelsquare.global.common_response;

import com.fasterxml.jackson.annotation.JsonInclude;

public record ApiResponse<T>(
	int code,
	String msg,
	@JsonInclude(JsonInclude.Include.NON_NULL)
	T data
) {

	public static <T> ApiResponse<T> of(
		StatusCode statusCode,
		T data) {
		return new ApiResponse(
			statusCode.getStatus().value(),
			statusCode.getMsg(),
			data
		);
	}

	public static <T> ApiResponse<T> of(
		StatusCode statusCode
	) {
		return new ApiResponse<>(
			statusCode.getStatus().value(),
			statusCode.getMsg(),
			null
		);
	}
}

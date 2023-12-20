package com.kernel360.kernelsquare.global.common_response;

import org.springframework.http.ResponseEntity;

public class ResponseEntityFactory<T> {
	public static ResponseEntity<ApiResponse> of(StatusCode statusCode) {
		return ResponseEntity.status(statusCode.getStatus()).body(ApiResponse.of(statusCode));
	}

	public static <T> ResponseEntity<ApiResponse<T>> of(StatusCode statusCode, T data) {
		return ResponseEntity.status(statusCode.getStatus()).body(ApiResponse.of(statusCode, data));
	}
}

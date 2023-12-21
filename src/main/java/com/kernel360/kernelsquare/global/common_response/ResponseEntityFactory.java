package com.kernel360.kernelsquare.global.common_response;

import org.springframework.http.ResponseEntity;

public class ResponseEntityFactory {
	public static ResponseEntity<ApiResponse> toResponseEntity(StatusCode statusCode) {
		return ResponseEntity.status(statusCode.getStatus()).body(ApiResponse.of(statusCode));
	}

	public static <T> ResponseEntity<ApiResponse<T>> toResponseEntity(StatusCode statusCode, T data) {
		return ResponseEntity.status(statusCode.getStatus()).body(ApiResponse.of(statusCode, data));
	}
}

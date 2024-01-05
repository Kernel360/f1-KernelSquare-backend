package com.kernel360.kernelsquare.global.common_response;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.kernel360.kernelsquare.global.common_response.service.code.CommonServiceStatus;
import com.kernel360.kernelsquare.global.util.ExceptionMessageConverter;

public class ResponseEntityFactory {
	public static ResponseEntity<ApiResponse> toResponseEntity(StatusCode statusCode) {
		return ResponseEntity.status(statusCode.getStatus()).body(ApiResponse.of(statusCode));
	}

	public static <T> ResponseEntity<ApiResponse<T>> toResponseEntity(StatusCode statusCode, T data) {
		return ResponseEntity.status(statusCode.getStatus()).body(ApiResponse.of(statusCode, data));
	}

	public static ResponseEntity<ApiResponse> toResponseEntity(MethodArgumentNotValidException e) {
		return ResponseEntity.badRequest()
			.body(new ApiResponse(CommonServiceStatus.VALIDATION_CHECK_FAIL.getServiceStatus(),
				ExceptionMessageConverter.buildMethodARgumentNotValidExceptionMsg(e), null));
	}
}

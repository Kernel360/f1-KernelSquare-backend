package com.kernelsquare.core.common_response;

import com.kernelsquare.core.common_response.error.code.ImageErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.kernelsquare.core.common_response.service.code.CommonServiceStatus;
import com.kernelsquare.core.util.ExceptionMessageConverter;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

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
				ExceptionMessageConverter.buildMethodArgumentNotValidExceptionMsg(e), null));
	}

	public static ResponseEntity<ApiResponse> toResponseEntity(MaxUploadSizeExceededException e) {
		return ResponseEntity.status(e.getStatusCode())
			.body(ApiResponse.of(ImageErrorCode.FILE_SIZE_EXCEEDED));
	}

	public static ResponseEntity<ApiResponse> toResponseEntity(Exception e) {
		return ResponseEntity.internalServerError()
			.body(new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null));
	}
}

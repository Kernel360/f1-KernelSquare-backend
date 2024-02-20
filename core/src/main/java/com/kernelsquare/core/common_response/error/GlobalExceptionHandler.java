package com.kernelsquare.core.common_response.error;

import com.kernelsquare.core.common_response.StatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiResponse> handleBusinessException(BusinessException e) {
		return ResponseEntityFactory.toResponseEntity(e.getErrorCode());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse> handleNotValidException(MethodArgumentNotValidException e) {
		return ResponseEntityFactory.toResponseEntity(e);
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ApiResponse> handleMaxSizeException(MaxUploadSizeExceededException e) {
		return ResponseEntityFactory.toResponseEntity(e);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException e) {
		return ResponseEntityFactory.toResponseEntity(e);
	}
}

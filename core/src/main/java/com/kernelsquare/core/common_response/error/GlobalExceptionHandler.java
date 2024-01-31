package com.kernelsquare.core.common_response.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;
import com.kernelsquare.core.common_response.error.exception.BusinessException;

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
}

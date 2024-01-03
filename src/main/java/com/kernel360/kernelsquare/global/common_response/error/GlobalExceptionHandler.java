package com.kernel360.kernelsquare.global.common_response.error;

import com.kernel360.kernelsquare.global.common_response.ApiResponse;
import com.kernel360.kernelsquare.global.common_response.ResponseEntityFactory;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiResponse> handleBusinessException(BusinessException e) {

		return ResponseEntityFactory.toResponseEntity(e.getErrorCode());
	}
}

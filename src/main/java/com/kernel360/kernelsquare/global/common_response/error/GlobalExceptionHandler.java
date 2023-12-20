package com.kernel360.kernelsquare.global.common_response.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.kernel360.kernelsquare.global.common_response.error.dto.ErrorResponse;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e,
		HttpServletRequest request) {
		return ResponseEntity.status(e.getErrorCode().getStatus())
			.body(ErrorResponse.of(e.getErrorCode()));
	}
}

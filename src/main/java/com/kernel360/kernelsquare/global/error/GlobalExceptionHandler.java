package com.kernel360.kernelsquare.global.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.kernel360.kernelsquare.global.dto.CommonApiResponse;
import com.kernel360.kernelsquare.global.error.exception.BusinessException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<CommonApiResponse<ErrorResponse>> handleBusinessException(BusinessException e,
		HttpServletRequest request) {
		return ResponseEntity.status(e.getErrorCode().getStatus())
			.body(CommonApiResponse.of(
				e.getErrorCode().getStatus(),
				e.getErrorCode().getMsg(),
				null));
	}
}

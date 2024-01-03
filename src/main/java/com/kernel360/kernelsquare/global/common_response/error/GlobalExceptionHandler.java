package com.kernel360.kernelsquare.global.common_response.error;

import com.kernel360.kernelsquare.global.common_response.ApiResponse;
import com.kernel360.kernelsquare.global.common_response.ResponseEntityFactory;
import com.kernel360.kernelsquare.global.common_response.error.code.CommonErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiResponse> handleBusinessException(BusinessException e) {

		return ResponseEntityFactory.toResponseEntity(e.getErrorCode());
	}
//
//	@ExceptionHandler(DataIntegrityViolationException.class)
//	public ResponseEntity<ApiResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
//
//		return ResponseEntityFactory.toResponseEntity(CommonErrorCode.DUPLICATE_DATA_EXIST);
//	}
}

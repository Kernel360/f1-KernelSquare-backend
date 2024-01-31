package com.kernelsquare.core.util;

import java.util.List;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

public class ExceptionMessageConverter {
	public static String buildMethodArgumentNotValidExceptionMsg(MethodArgumentNotValidException e) {
		StringBuilder stringBuilder = new StringBuilder();
		List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();

		for (FieldError fieldError : fieldErrors) {
			String fieldName = fieldError.getField();
			String errorMessage = fieldError.getDefaultMessage();

			stringBuilder.append(fieldName + " : " + errorMessage).append("\n");
		}

		return stringBuilder.toString();
	}
}

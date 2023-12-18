package com.kernel360.kernelsquare.global.error.dto;

import org.springframework.http.HttpStatus;

import com.kernel360.kernelsquare.global.error.code.ErrorCode;

public record ErrorResponse(
	HttpStatus code,
	String msg
) {
	public static ErrorResponse of(ErrorCode errorCode) {
		return new ErrorResponse(
			errorCode.getStatus(),
			errorCode.getMsg()
		);
	}
}

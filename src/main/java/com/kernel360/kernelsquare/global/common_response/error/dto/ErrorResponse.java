package com.kernel360.kernelsquare.global.common_response.error.dto;

import com.kernel360.kernelsquare.global.common_response.error.code.ErrorCode;

public record ErrorResponse(
	int code,
	String msg
) {
	public static ErrorResponse of(ErrorCode errorCode) {
		return new ErrorResponse(
			errorCode.getStatus().value(),
			errorCode.getMsg()
		);
	}
}

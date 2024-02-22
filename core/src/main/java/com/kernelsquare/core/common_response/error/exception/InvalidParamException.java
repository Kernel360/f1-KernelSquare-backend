package com.kernelsquare.core.common_response.error.exception;

import com.kernelsquare.core.common_response.error.code.CommonErrorCode;
import com.kernelsquare.core.common_response.error.code.ErrorCode;

import lombok.Getter;

@Getter
public class InvalidParamException extends RuntimeException {
	private ErrorCode errorCode;

	public InvalidParamException(String message) {
		super(message);
		this.errorCode = CommonErrorCode.INVALID_PARAMETER;
	}
}

package com.kernel360.kernelsquare.global.error.code;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
	HttpStatus getStatus();

	String getMsg();
}

package com.kernel360.kernelsquare.global.common_response;

import org.springframework.http.HttpStatus;

public interface StatusCode {
	HttpStatus getStatus();

	Integer getCode();

	String getMsg();
}

package com.kernelsquare.core.common_response;

import org.springframework.http.HttpStatus;

public interface StatusCode {
	HttpStatus getStatus();

	Integer getCode();

	String getMsg();
}

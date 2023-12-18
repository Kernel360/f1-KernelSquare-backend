package com.kernel360.kernelsquare.global.dto;

import org.springframework.http.HttpStatus;

public record CommonApiResponse<D>(
	HttpStatus code,
	String msg,
	D data
) {

	public static <D> CommonApiResponse<D> of(
		HttpStatus code,
		String msg,
		D data) {
		return new CommonApiResponse(
			code,
			msg,
			data
		);
	}
}

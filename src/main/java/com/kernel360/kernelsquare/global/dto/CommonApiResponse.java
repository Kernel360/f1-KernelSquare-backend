package com.kernel360.kernelsquare.global.dto;

public record CommonApiResponse<D>(
	String code,
	String msg,
	D data
) {

	public static <D> CommonApiResponse<D> of(
		String code,
		String msg,
		D data) {
		return new CommonApiResponse(
			code,
			msg,
			data
		);
	}
}

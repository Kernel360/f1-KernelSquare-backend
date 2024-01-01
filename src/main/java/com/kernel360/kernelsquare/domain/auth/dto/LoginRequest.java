package com.kernel360.kernelsquare.domain.auth.dto;

public record LoginRequest(
	String email,
	String password
) {
}

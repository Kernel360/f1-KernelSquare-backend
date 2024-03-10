package com.kernelsquare.adminapi.domain.auth.controller;

import com.kernelsquare.adminapi.domain.auth.dto.LoginRequest;
import com.kernelsquare.adminapi.domain.auth.dto.LoginResponse;
import com.kernelsquare.adminapi.domain.auth.dto.TokenRequest;
import com.kernelsquare.adminapi.domain.auth.dto.TokenResponse;
import com.kernelsquare.adminapi.domain.auth.service.AuthService;
import com.kernelsquare.adminapi.domain.auth.service.TokenProvider;
import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;
import com.kernelsquare.core.validation.ValidationSequence;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.kernelsquare.core.common_response.response.code.AuthResponseCode.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;
	private final TokenProvider tokenProvider;

	@PostMapping("/auth/login")
	public ResponseEntity<ApiResponse<LoginResponse>> login(
		final @RequestBody @Validated(ValidationSequence.class) LoginRequest loginRequest) {
		Member member = authService.login(loginRequest);
		TokenResponse tokenResponse = tokenProvider.createToken(member, loginRequest);
		LoginResponse loginResponse = LoginResponse.of(member, tokenResponse);
		return ResponseEntityFactory.toResponseEntity(LOGIN_SUCCESS, loginResponse);
	}

	@PostMapping("/auth/reissue")
	public ResponseEntity<ApiResponse<TokenResponse>> reissueAccessToken(
		final @RequestBody TokenRequest requestTokenRequest) {
		TokenResponse tokenResponse = tokenProvider.reissueToken(requestTokenRequest);
		return ResponseEntityFactory.toResponseEntity(ACCESS_TOKEN_REISSUED, tokenResponse);
	}

	@PostMapping("/auth/logout")
	public ResponseEntity<ApiResponse> logout(final @RequestBody TokenRequest tokenRequest) {
		tokenProvider.logout(tokenRequest);
		return ResponseEntityFactory.toResponseEntity(LOGOUT_SUCCESS);
	}
}

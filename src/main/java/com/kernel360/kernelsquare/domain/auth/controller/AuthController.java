package com.kernel360.kernelsquare.domain.auth.controller;

import static com.kernel360.kernelsquare.global.common_response.response.code.AuthResponseCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kernel360.kernelsquare.domain.auth.dto.CheckDuplicateEmailRequest;
import com.kernel360.kernelsquare.domain.auth.dto.CheckDuplicateNicknameRequest;
import com.kernel360.kernelsquare.domain.auth.dto.LoginRequest;
import com.kernel360.kernelsquare.domain.auth.dto.LoginResponse;
import com.kernel360.kernelsquare.domain.auth.dto.SignUpRequest;
import com.kernel360.kernelsquare.domain.auth.dto.SignUpResponse;
import com.kernel360.kernelsquare.domain.auth.dto.TokenRequest;
import com.kernel360.kernelsquare.domain.auth.dto.TokenResponse;
import com.kernel360.kernelsquare.domain.auth.service.AuthService;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.global.common_response.ApiResponse;
import com.kernel360.kernelsquare.global.common_response.ResponseEntityFactory;
import com.kernel360.kernelsquare.global.jwt.TokenProvider;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;
	private final TokenProvider tokenProvider;

	@PostMapping("/auth/login")
	public ResponseEntity<ApiResponse<LoginResponse>> login(final @RequestBody @Valid LoginRequest loginRequest) {
		Member member = authService.login(loginRequest);
		TokenResponse tokenResponse = tokenProvider.createToken(member, loginRequest);
		LoginResponse loginResponse = LoginResponse.of(member, tokenResponse);
		return ResponseEntityFactory.toResponseEntity(LOGIN_SUCCESS, loginResponse);
	}

	@PostMapping("/auth/signup")
	public ResponseEntity<ApiResponse<SignUpResponse>> signUp(final @RequestBody @Valid SignUpRequest signUpRequest) {
		SignUpResponse signUpResponse = authService.signUp(signUpRequest);
		return ResponseEntityFactory.toResponseEntity(SIGN_UP_SUCCESS, signUpResponse);
	}

	@PostMapping("/auth/reissue")
	public ResponseEntity<ApiResponse<TokenResponse>> reissueAccessToken(
		final @RequestBody TokenRequest requestTokenRequest) {
		TokenResponse tokenResponse = tokenProvider.reissueToken(requestTokenRequest);
		return ResponseEntityFactory.toResponseEntity(ACCESS_TOKEN_REISSUED, tokenResponse);
	}

	@PostMapping("/auth/check/email")
	public ResponseEntity<ApiResponse> isEmailUnique(
		final @Valid @RequestBody CheckDuplicateEmailRequest emailRequest) {
		authService.isEmailUnique(emailRequest.email());
		return ResponseEntityFactory.toResponseEntity(EMAIL_UNIQUE_VALIDATED);
	}

	@PostMapping("/auth/check/nickname")
	public ResponseEntity<ApiResponse> isNicknameUnique(
		final @Valid @RequestBody CheckDuplicateNicknameRequest nicknameRequest) {
		authService.isNicknameUnique(nicknameRequest.nickname());
		return ResponseEntityFactory.toResponseEntity(NICKNAME_UNIQUE_VALIDATED);
	}

	@PostMapping("/auth/logout")
	public ResponseEntity<ApiResponse> logout(final @RequestBody TokenRequest tokenRequest) {
		tokenProvider.logout(tokenRequest);
		return ResponseEntityFactory.toResponseEntity(LOGOUT_SUCCESS);
	}
}

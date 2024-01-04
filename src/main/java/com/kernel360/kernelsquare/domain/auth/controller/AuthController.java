package com.kernel360.kernelsquare.domain.auth.controller;

import static com.kernel360.kernelsquare.global.common_response.response.code.AuthResponseCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kernel360.kernelsquare.domain.auth.dto.LoginRequest;
import com.kernel360.kernelsquare.domain.auth.dto.LoginResponse;
import com.kernel360.kernelsquare.domain.auth.dto.SignUpRequest;
import com.kernel360.kernelsquare.domain.auth.dto.SignUpResponse;
import com.kernel360.kernelsquare.domain.auth.dto.TokenDto;
import com.kernel360.kernelsquare.domain.auth.service.AuthService;
import com.kernel360.kernelsquare.global.common_response.ApiResponse;
import com.kernel360.kernelsquare.global.common_response.ResponseEntityFactory;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@PostMapping("/auth/login")
	public ResponseEntity<ApiResponse<LoginResponse>> login(final @RequestBody @Valid LoginRequest loginRequest) {
		LoginResponse loginResponse = authService.login(loginRequest);
		return ResponseEntityFactory.toResponseEntity(LOGIN_SUCCESS, loginResponse);
	}

	@PostMapping("/auth/signup")
	public ResponseEntity<ApiResponse<SignUpResponse>> signUp(final @RequestBody @Valid SignUpRequest signUpRequest) {
		SignUpResponse signUpResponse = authService.signUp(signUpRequest);
		return ResponseEntityFactory.toResponseEntity(SIGN_UP_SUCCESS, signUpResponse);
	}

	@PostMapping("/auth/reissue")
	public ResponseEntity<ApiResponse<TokenDto>> reissueAccessToken(final @RequestBody TokenDto requestTokenDto) {
		TokenDto tokenDto = authService.reissueAccessToken(requestTokenDto);
		return ResponseEntityFactory.toResponseEntity(ACCESS_TOKEN_REISSUED, tokenDto);
	}

	@PostMapping("/auth/check/email")
	public ResponseEntity<ApiResponse> isEmailUnique(final @RequestBody SignUpRequest signUpRequest) {
		authService.isEmailUnique(signUpRequest.email());
		return ResponseEntityFactory.toResponseEntity(EMAIL_UNIQUE_VALIDATED);
	}

	@PostMapping("/auth/check/nickname")
	public ResponseEntity<ApiResponse> isNicknameUnique(final @RequestBody SignUpRequest signUpRequest) {
		authService.isNicknameUnique(signUpRequest.nickname());
		return ResponseEntityFactory.toResponseEntity(NICKNAME_UNIQUE_VALIDATED);
	}

	@PostMapping("/auth/logout")
	public ResponseEntity<ApiResponse> logout(final @RequestBody TokenDto tokenDto) {
		authService.logout(tokenDto);
		return ResponseEntityFactory.toResponseEntity(LOGOUT_SUCCESS);
	}
}

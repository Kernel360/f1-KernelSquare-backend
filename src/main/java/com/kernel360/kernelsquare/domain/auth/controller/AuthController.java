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
import com.kernel360.kernelsquare.domain.auth.dto.TokenDto;
import com.kernel360.kernelsquare.domain.auth.entity.RefreshToken;
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
		TokenDto tokenDto = tokenProvider.createToken(member, loginRequest);
		LoginResponse loginResponse = LoginResponse.of(member, tokenDto);
		return ResponseEntityFactory.toResponseEntity(LOGIN_SUCCESS, loginResponse);
	}

	@PostMapping("/auth/signup")
	public ResponseEntity<ApiResponse<SignUpResponse>> signUp(final @RequestBody @Valid SignUpRequest signUpRequest) {
		SignUpResponse signUpResponse = authService.signUp(signUpRequest);
		return ResponseEntityFactory.toResponseEntity(SIGN_UP_SUCCESS, signUpResponse);
	}

	@PostMapping("/auth/reissue")
	public ResponseEntity<ApiResponse<TokenDto>> reissueAccessToken(final @RequestBody TokenDto requestTokenDto) {
		TokenDto tokenDto = tokenProvider.reissueToken(requestTokenDto);
		return ResponseEntityFactory.toResponseEntity(ACCESS_TOKEN_REISSUED, tokenDto);
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
	public ResponseEntity<ApiResponse> logout(final @RequestBody TokenDto tokenDto) {
		RefreshToken refreshToken = tokenProvider.toRefreshToken(tokenDto.refreshToken());
		tokenProvider.removeRedisRefreshToken(refreshToken.getMemberId());
		return ResponseEntityFactory.toResponseEntity(LOGOUT_SUCCESS);
	}
}

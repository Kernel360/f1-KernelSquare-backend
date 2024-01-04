package com.kernel360.kernelsquare.domain.auth.service;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kernel360.kernelsquare.domain.auth.dto.LoginRequest;
import com.kernel360.kernelsquare.domain.auth.dto.LoginResponse;
import com.kernel360.kernelsquare.domain.auth.dto.SignUpRequest;
import com.kernel360.kernelsquare.domain.auth.dto.SignUpResponse;
import com.kernel360.kernelsquare.domain.auth.dto.TokenDto;
import com.kernel360.kernelsquare.domain.auth.entity.RefreshToken;
import com.kernel360.kernelsquare.domain.authority.entity.Authority;
import com.kernel360.kernelsquare.domain.authority.repository.AuthorityRepository;
import com.kernel360.kernelsquare.domain.level.entity.Level;
import com.kernel360.kernelsquare.domain.level.repository.LevelRepository;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.member.repository.MemberRepository;
import com.kernel360.kernelsquare.domain.member_authority.entity.MemberAuthority;
import com.kernel360.kernelsquare.domain.member_authority.repository.MemberAuthorityRepository;
import com.kernel360.kernelsquare.global.common_response.error.code.AuthErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.code.AuthorityErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.code.LevelErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;
import com.kernel360.kernelsquare.global.domain.AuthorityType;
import com.kernel360.kernelsquare.global.jwt.TokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final PasswordEncoder passwordEncoder;
	private final TokenProvider tokenProvider;
	private final MemberRepository memberRepository;
	private final MemberAuthorityRepository memberAuthorityRepository;
	private final AuthorityRepository authorityRepository;
	private final LevelRepository levelRepository;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;

	@Transactional(readOnly = true)
	public LoginResponse login(final LoginRequest loginRequest) {
		Member findMember = memberRepository.findByEmail(loginRequest.email())
			.orElseThrow(() -> new BusinessException(AuthErrorCode.INVALID_ACCOUNT));
		validateLogin(loginRequest, findMember);
		UsernamePasswordAuthenticationToken authenticationToken =
			new UsernamePasswordAuthenticationToken(findMember.getId(), loginRequest.password());
		Authentication authentication = authenticationManagerBuilder.getObject()
			.authenticate(authenticationToken);
		TokenDto tokenDto = tokenProvider.createToken(authentication);

		return LoginResponse.of(findMember, authentication.getAuthorities(), tokenDto);
	}

	@Transactional
	public SignUpResponse signUp(final SignUpRequest signUpRequest) {
		Level level = levelRepository.findLevelByName(1L)
			.orElseThrow(() -> new BusinessException(LevelErrorCode.LEVEL_NOT_FOUND));
		String encodedPassword = passwordEncoder.encode(signUpRequest.password());
		Member savedMember = memberRepository.save(SignUpRequest.toEntity(signUpRequest, encodedPassword, level));

		Authority authority = authorityRepository.findAuthorityByAuthorityType(AuthorityType.ROLE_USER)
			.orElseThrow(() -> new BusinessException(AuthorityErrorCode.AUTHORITY_NOT_FOUND));
		MemberAuthority memberAuthority = MemberAuthority.builder().member(savedMember).authority(authority).build();
		memberAuthorityRepository.save(memberAuthority);

		List<MemberAuthority> authorities = List.of(memberAuthority);
		savedMember.initAuthorities(authorities);

		return SignUpResponse.of(savedMember);
	}

	private void validateLogin(LoginRequest loginRequest, Member member) {
		if (!passwordEncoder.matches(loginRequest.password(), member.getPassword())) {
			throw new BusinessException(AuthErrorCode.INVALID_PASSWORD);
		}
	}

	public void logout(final TokenDto tokenDto) {
		RefreshToken refreshToken = tokenProvider.toRefreshToken(tokenDto.refreshToken());
		Long memberId = refreshToken.getMemberId();
		tokenProvider.removeRedisRefreshToken(memberId);
	}

	public TokenDto reissueAccessToken(final TokenDto requestTokenDto) {
		return tokenProvider.reissueToken(requestTokenDto);
	}

	@Transactional(readOnly = true)
	public void isEmailUnique(final String email) {
		if (memberRepository.existsByEmail(email)) {
			throw new BusinessException(AuthErrorCode.ALREADY_SAVED_EMAIL);
		}
	}

	@Transactional(readOnly = true)
	public void isNicknameUnique(final String nickname) {
		if (memberRepository.existsByNickname(nickname)) {
			throw new BusinessException(AuthErrorCode.ALREADY_SAVED_NICKNAME);
		}
	}
}

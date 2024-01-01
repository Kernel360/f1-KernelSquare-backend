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
import com.kernel360.kernelsquare.domain.auth.dto.TokenDto;
import com.kernel360.kernelsquare.domain.authority.entity.Authority;
import com.kernel360.kernelsquare.domain.authority.repository.AuthorityRepository;
import com.kernel360.kernelsquare.domain.level.entity.Level;
import com.kernel360.kernelsquare.domain.level.repository.LevelRepository;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.member.repository.MemberRepository;
import com.kernel360.kernelsquare.domain.member_authority.entity.MemberAuthority;
import com.kernel360.kernelsquare.domain.member_authority.repository.MemberAuthorityRepository;
import com.kernel360.kernelsquare.global.common_response.error.code.AuthErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;
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

	public LoginResponse authenticate(LoginRequest loginRequest) {
		Member findMember = memberRepository.findByEmail(loginRequest.email())
			.orElseThrow(() -> new BusinessException(AuthErrorCode.INVALID_ACCOUNT));
		validateLogin(loginRequest, findMember);
		UsernamePasswordAuthenticationToken authenticationToken =
			new UsernamePasswordAuthenticationToken(findMember.getId(), loginRequest.password());
		Authentication authentication = authenticationManagerBuilder.getObject()
			.authenticate(authenticationToken);
		TokenDto tokenDto = tokenProvider.createToken(authentication);

		return LoginResponse.of(findMember.getNickname(), authentication.getAuthorities(), tokenDto);
	}

	@Transactional
	public Long signUp(SignUpRequest signUpRequest) {
		//todo : 에러 처리 해야함
		Level level = levelRepository.findLevelByName("level 1").orElseThrow(() -> new RuntimeException());
		String encodedPassword = passwordEncoder.encode(signUpRequest.password());
		Member savedMember = memberRepository.save(SignUpRequest.toEntity(signUpRequest, encodedPassword, level));

		//todo : 에러 처리...
		Authority authority = authorityRepository.findAuthorityByAuthorityType("ROLE_USER")
			.orElseThrow(() -> new RuntimeException());
		MemberAuthority memberAuthority = MemberAuthority.builder().member(savedMember).authority(authority).build();
		memberAuthorityRepository.save(memberAuthority);

		List<MemberAuthority> authorities = List.of(memberAuthority);
		savedMember.initAuthorities(authorities);

		return savedMember.getId();
	}

	private void validateLogin(LoginRequest loginRequest, Member member) {
		if (!passwordEncoder.matches(loginRequest.password(), member.getPassword())) {
			throw new BusinessException(AuthErrorCode.INVALID_PASSWORD);
		}
	}

	public TokenDto reissueAccessToken(TokenDto requestTokenDto) {
		return tokenProvider.reissueToken(requestTokenDto);
	}

	public void isEmailUnique(String email) {
		if (memberRepository.existsByEmail(email)) {
			throw new BusinessException(AuthErrorCode.ALREADY_SAVED_EMAIL);
		}
	}

	public void isNicknameUnique(String nickname) {
		if (memberRepository.existsByNickname(nickname)) {
			throw new BusinessException(AuthErrorCode.ALREADY_SAVED_NICKNAME);
		}
	}
}

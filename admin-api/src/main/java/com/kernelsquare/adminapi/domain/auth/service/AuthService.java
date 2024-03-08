package com.kernelsquare.adminapi.domain.auth.service;

import com.kernelsquare.adminapi.domain.auth.dto.LoginRequest;
import com.kernelsquare.adminapi.domain.auth.dto.SignUpRequest;
import com.kernelsquare.adminapi.domain.auth.dto.SignUpResponse;
import com.kernelsquare.adminapi.domain.auth.validation.AuthValidation;
import com.kernelsquare.core.common_response.error.code.AuthErrorCode;
import com.kernelsquare.core.common_response.error.code.AuthorityErrorCode;
import com.kernelsquare.core.common_response.error.code.LevelErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.core.type.AuthorityType;
import com.kernelsquare.domainmysql.domain.authority.entity.Authority;
import com.kernelsquare.domainmysql.domain.authority.repository.AuthorityRepository;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.level.repository.LevelRepository;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberReader;
import com.kernelsquare.domainmysql.domain.member.repository.MemberRepository;
import com.kernelsquare.domainmysql.domain.member_authority.entity.MemberAuthority;
import com.kernelsquare.domainmysql.domain.member_authority.repository.MemberAuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;
	private final MemberAuthorityRepository memberAuthorityRepository;
	private final AuthorityRepository authorityRepository;
	private final LevelRepository levelRepository;
	private final MemberReader memberReader;

	@Transactional
	public Member login(final LoginRequest loginRequest) {
		Member findMember = memberReader.findMember(loginRequest.email());

		AuthValidation.validatePassword(passwordEncoder, loginRequest.password(), findMember.getPassword());

		AuthValidation.validateAdminAuthority(findMember);

		return findMember;
	}

	@Transactional
	public SignUpResponse signUp(final SignUpRequest signUpRequest) {
		Level level = levelRepository.findByName(1L)
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

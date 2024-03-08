package com.kernelsquare.adminapi.domain.auth.service;

import com.kernelsquare.adminapi.domain.auth.dto.LoginRequest;
import com.kernelsquare.adminapi.domain.auth.validation.AuthValidation;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberReader;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final PasswordEncoder passwordEncoder;
	private final MemberReader memberReader;

	@Transactional
	public Member login(final LoginRequest loginRequest) {
		Member findMember = memberReader.findMember(loginRequest.email());

		AuthValidation.validatePassword(passwordEncoder, loginRequest.password(), findMember.getPassword());

		AuthValidation.validateAdminAuthority(findMember);

		return findMember;
	}
}

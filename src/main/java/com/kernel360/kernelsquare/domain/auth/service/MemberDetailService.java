package com.kernel360.kernelsquare.domain.auth.service;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.member.repository.MemberRepository;
import com.kernel360.kernelsquare.domain.member_authority.entity.MemberAuthority;
import com.kernel360.kernelsquare.global.common_response.error.code.MemberErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberDetailService implements UserDetailsService {
	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		String id, password;
		List<SimpleGrantedAuthority> authorities;
		Member member = memberRepository.findById(Long.parseLong(username)).orElseThrow(() -> new BusinessException(
			MemberErrorCode.MEMBER_NOT_FOUND));

		id = String.valueOf(member.getId());
		password = member.getPassword();
		authorities = member
			.getAuthorities()
			.stream()
			.map(MemberAuthority::getAuthority)
			.map(authority ->
				new SimpleGrantedAuthority(authority
					.getAuthorityType()
					.getDescription()))
			.toList();
		return User.builder()
			.username(id)
			.password(password)
			.authorities(authorities)
			.build();
	}
}

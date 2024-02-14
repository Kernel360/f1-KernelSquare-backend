package com.kernelsquare.memberapi.domain.auth.service;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kernelsquare.core.common_response.error.code.MemberErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberRepository;
import com.kernelsquare.domainmysql.domain.member_authority.entity.MemberAuthority;
import com.kernelsquare.memberapi.domain.auth.dto.MemberPrincipal;

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
		return new MemberPrincipal(member.getId(), member.getPassword(), authorities);
	}
}

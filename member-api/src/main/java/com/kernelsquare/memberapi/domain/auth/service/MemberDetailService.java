package com.kernelsquare.memberapi.domain.auth.service;

import com.kernelsquare.core.common_response.error.code.MemberErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberRepository;
import com.kernelsquare.domainmysql.domain.member_authority.entity.MemberAuthority;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberDetailService implements UserDetailsService {
	private final MemberRepository memberRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		Member member = memberRepository.findById(Long.parseLong(username)).orElseThrow(() -> new BusinessException(
			MemberErrorCode.MEMBER_NOT_FOUND));

//		log.info("loadUserByUsername에서 확인해보자. : " + member
//			.getAuthorities()
//			.stream()
//			.map(MemberAuthority::getAuthority)
//			.map(authority ->
//				new SimpleGrantedAuthority(authority
//					.getAuthorityType()
//					.getDescription()))
//			.toList());

		List<SimpleGrantedAuthority> authorities = member
			.getAuthorities()
			.stream()
			.map(MemberAuthority::getAuthority)
			.map(authority ->
				new SimpleGrantedAuthority(authority
					.getAuthorityType()
					.getDescription()))
			.toList();

//		log.info("loadUserByUsername에서 확인해보자. : " + member.getLevel());

		Level level = member.getLevel();

		return new MemberAdapter(member);
	}
}

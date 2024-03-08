package com.kernelsquare.adminapi.domain.auth.service;

import com.kernelsquare.adminapi.domain.auth.dto.MemberAdapter;
import com.kernelsquare.adminapi.domain.auth.dto.MemberAdaptorInstance;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberReader;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberDetailService implements UserDetailsService {
	private final MemberReader memberReader;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		Member member = memberReader.findMember(Long.parseLong(username));

		Hibernate.initialize(member.getLevel());
		Hibernate.initialize(member.getAuthorities());

		MemberAdaptorInstance memberInstance = MemberAdaptorInstance.of(member);

		return new MemberAdapter(memberInstance);
	}
}

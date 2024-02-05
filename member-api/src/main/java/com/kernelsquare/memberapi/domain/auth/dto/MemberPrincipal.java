package com.kernelsquare.memberapi.domain.auth.dto;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.kernelsquare.domainmysql.domain.member.entity.Member;

import lombok.Getter;

@Getter
public class MemberPrincipal extends User {
	private Member member;

	public MemberPrincipal(Member member, List<? extends GrantedAuthority> authorities) {
		super(String.valueOf(member.getId()), member.getPassword(),
			authorities);
		this.member = member;
	}
}

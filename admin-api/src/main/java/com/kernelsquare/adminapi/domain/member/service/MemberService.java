package com.kernelsquare.adminapi.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kernelsquare.adminapi.domain.member.dto.FindMemberResponse;
import com.kernelsquare.core.common_response.error.code.MemberErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;

	@Transactional(readOnly = true)
	public FindMemberResponse findMember(Long id) {
		Member member = getMemberById(id);
		return FindMemberResponse.from(member);
	}

	@Transactional
	public void deleteMember(Long id) {
		memberRepository.deleteById(id);
	}

	private Member getMemberById(Long id) {
		return memberRepository.findById(id)
			.orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
	}
}

package com.kernel360.kernelsquare.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kernel360.kernelsquare.domain.member.dto.FindMemberResponse;
import com.kernel360.kernelsquare.domain.member.dto.UpdateMemberRequest;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.member.repository.MemberRepository;
import com.kernel360.kernelsquare.global.error.code.MemberErrorCode;
import com.kernel360.kernelsquare.global.error.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;

	@Transactional
	public void updateMember(Long id, UpdateMemberRequest updateMemberRequest) {
		Member member = getMemberById(id);
		member.updateImageUrl(updateMemberRequest.imageUrl(), updateMemberRequest.introduction());
	}

	@Transactional(readOnly = true)
	public FindMemberResponse findMember(Long id) {
		Member member = getMemberById(id);
		return FindMemberResponse.from(member);
	}

	@Transactional
	public void updateMemberPassword(Long id, String password) {
		Member member = getMemberById(id);
		member.updatePassword(password);
	}

	@Transactional
	public void deleteMember(Long id) {
		Member member = getMemberById(id);
		memberRepository.delete(member);
	}

	private Member getMemberById(Long id) {
		return memberRepository.findById(id)
			.orElseThrow(() -> new BusinessException(MemberErrorCode.NOT_FOUND_MEMBER));
	}
}

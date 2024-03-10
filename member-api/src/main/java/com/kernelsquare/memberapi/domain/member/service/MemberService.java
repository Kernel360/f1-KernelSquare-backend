package com.kernelsquare.memberapi.domain.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kernelsquare.core.common_response.error.code.MemberErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberRepository;
import com.kernelsquare.core.util.ImageUtils;
import com.kernelsquare.memberapi.domain.member.dto.FindMemberResponse;
import com.kernelsquare.memberapi.domain.member.dto.UpdateMemberIntroductionRequest;
import com.kernelsquare.memberapi.domain.member.dto.UpdateMemberProfileRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public void updateMemberIntroduction(Long id, UpdateMemberIntroductionRequest updateMemberIntroductionRequest) {
		Member member = getMemberById(id);
		member.updateIntroduction(updateMemberIntroductionRequest.introduction());
	}

	@Transactional
	public void updateMemberProfile(Long id, UpdateMemberProfileRequest updateMemberProfileRequest) {
		Member member = getMemberById(id);
		member.updateImageUrl(ImageUtils.parseFilePath(updateMemberProfileRequest.imageUrl()));
	}

	@Transactional(readOnly = true)
	public FindMemberResponse findMember(Long id) {
		Member member = getMemberById(id);
		return FindMemberResponse.from(member);
	}

	@Transactional
	public void updateMemberPassword(Long id, String password) {
		Member member = getMemberById(id);
		member.updatePassword(passwordEncoder.encode(password));
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

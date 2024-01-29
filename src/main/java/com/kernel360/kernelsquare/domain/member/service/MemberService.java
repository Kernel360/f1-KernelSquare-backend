package com.kernel360.kernelsquare.domain.member.service;

import com.kernel360.kernelsquare.domain.image.utils.ImageUtils;
import com.kernel360.kernelsquare.domain.member.dto.UpdateMemberIntroductionRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kernel360.kernelsquare.domain.member.dto.FindMemberResponse;
import com.kernel360.kernelsquare.domain.member.dto.UpdateMemberProfileRequest;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.member.repository.MemberRepository;
import com.kernel360.kernelsquare.global.common_response.error.code.MemberErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;

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

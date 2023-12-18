package com.kernel360.kernelsquare.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kernel360.kernelsquare.domain.member.dto.FindMemberResponse;
import com.kernel360.kernelsquare.domain.member.dto.UpdateMemberRequest;
import com.kernel360.kernelsquare.domain.member.dto.UpdateMemberResponse;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;

	@Transactional
	public UpdateMemberResponse update(Long id, UpdateMemberRequest updateMemberRequest) {
		Member member = getMember(id);
		member.updateImageUrl(updateMemberRequest.imageUrl());
		return UpdateMemberResponse.from(member);
	}

	@Transactional(readOnly = true)
	public FindMemberResponse find(Long id) {
		Member member = getMember(id);
		return FindMemberResponse.from(member);
	}

	@Transactional
	public void delete(Long id) {
		Member member = getMember(id);
		memberRepository.delete(member);
	}

	private Member getMember(Long id) {
		return memberRepository.findById(id)
			.orElseThrow(() -> new RuntimeException());
	}
}

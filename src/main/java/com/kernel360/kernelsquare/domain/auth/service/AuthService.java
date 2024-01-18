package com.kernel360.kernelsquare.domain.auth.service;

import java.util.List;

import com.kernel360.kernelsquare.domain.member.service.MemberService;
import com.kernel360.kernelsquare.global.util.experience.ExperiencePolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kernel360.kernelsquare.domain.auth.dto.LoginRequest;
import com.kernel360.kernelsquare.domain.auth.dto.SignUpRequest;
import com.kernel360.kernelsquare.domain.auth.dto.SignUpResponse;
import com.kernel360.kernelsquare.domain.authority.entity.Authority;
import com.kernel360.kernelsquare.domain.authority.repository.AuthorityRepository;
import com.kernel360.kernelsquare.domain.level.entity.Level;
import com.kernel360.kernelsquare.domain.level.repository.LevelRepository;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.member.repository.MemberRepository;
import com.kernel360.kernelsquare.domain.member_authority.entity.MemberAuthority;
import com.kernel360.kernelsquare.domain.member_authority.repository.MemberAuthorityRepository;
import com.kernel360.kernelsquare.global.common_response.error.code.AuthErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.code.AuthorityErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.code.LevelErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;
import com.kernel360.kernelsquare.global.domain.AuthorityType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;
	private final MemberAuthorityRepository memberAuthorityRepository;
	private final AuthorityRepository authorityRepository;
	private final LevelRepository levelRepository;

	@Transactional
	public Member login(final LoginRequest loginRequest) {
		Member findMember = memberRepository.findByEmail(loginRequest.email())
			.orElseThrow(() -> new BusinessException(AuthErrorCode.INVALID_ACCOUNT));
		if (!passwordEncoder.matches(loginRequest.password(), findMember.getPassword())) {
			throw new BusinessException(AuthErrorCode.INVALID_PASSWORD);
		}
		findMember.addExperience(ExperiencePolicy.MEMBER_DAILY_ATTENDED.getReward());
		if (findMember.isExperienceExceed(findMember.getExperience())) {
			findMember.updateExperience(findMember.getExperience() - findMember.getLevel().getLevelUpperLimit());
			Level nextLevel = levelRepository.findByName(findMember.getLevel().getName() + 1)
					.orElseThrow(() -> new BusinessException(LevelErrorCode.LEVEL_NOT_FOUND));
			findMember.updateLevel(nextLevel);
		}
		return findMember;
	}

	@Transactional
	public SignUpResponse signUp(final SignUpRequest signUpRequest) {
		Level level = levelRepository.findByName(1L)
			.orElseThrow(() -> new BusinessException(LevelErrorCode.LEVEL_NOT_FOUND));
		String encodedPassword = passwordEncoder.encode(signUpRequest.password());
		Member savedMember = memberRepository.save(SignUpRequest.toEntity(signUpRequest, encodedPassword, level));

		Authority authority = authorityRepository.findAuthorityByAuthorityType(AuthorityType.ROLE_USER)
			.orElseThrow(() -> new BusinessException(AuthorityErrorCode.AUTHORITY_NOT_FOUND));
		MemberAuthority memberAuthority = MemberAuthority.builder().member(savedMember).authority(authority).build();
		memberAuthorityRepository.save(memberAuthority);

		List<MemberAuthority> authorities = List.of(memberAuthority);
		savedMember.initAuthorities(authorities);

		return SignUpResponse.of(savedMember);
	}

	@Transactional(readOnly = true)
	public void isEmailUnique(final String email) {
		if (memberRepository.existsByEmail(email)) {
			throw new BusinessException(AuthErrorCode.ALREADY_SAVED_EMAIL);
		}
	}

	@Transactional(readOnly = true)
	public void isNicknameUnique(final String nickname) {
		if (memberRepository.existsByNickname(nickname)) {
			throw new BusinessException(AuthErrorCode.ALREADY_SAVED_NICKNAME);
		}
	}

	private void updateMemberExperienceByAction(Member member, Long diffExp) {
		Long memberExperience = member.getExperience() + diffExp;
		Long upperLimit = member.getLevel().getLevelUpperLimit();

		if (memberExperience >= upperLimit) {
			Level currentLevel = member.getLevel();
			Level nextLevel = levelRepository.findByName(currentLevel.getName() + 1)
					.orElseThrow(() -> new BusinessException(LevelErrorCode.LEVEL_NOT_FOUND));
			member.updateLevel(nextLevel);
			member.updateExperience(memberExperience - upperLimit);
			memberRepository.save(member);
			return;
		}
		member.updateExperience(memberExperience);
		memberRepository.save(member);
	}
}

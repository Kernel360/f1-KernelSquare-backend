package com.kernelsquare.memberapi.domain.auth.service;

import java.util.List;

import com.kernelsquare.core.annotations.LogExecutionTime;
import com.kernelsquare.domainmysql.domain.auth.command.AuthCommand;
import com.kernelsquare.domainmysql.domain.member.info.MemberInfo;
import com.kernelsquare.domainmysql.domain.member.repository.MemberReader;
import com.kernelsquare.memberapi.domain.auth.validation.AuthValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kernelsquare.core.common_response.error.code.AuthErrorCode;
import com.kernelsquare.core.common_response.error.code.AuthorityErrorCode;
import com.kernelsquare.core.common_response.error.code.LevelErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.core.type.AuthorityType;
import com.kernelsquare.core.util.ExperiencePolicy;
import com.kernelsquare.domainmysql.domain.authority.entity.Authority;
import com.kernelsquare.domainmysql.domain.authority.repository.AuthorityRepository;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.level.repository.LevelRepository;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberRepository;
import com.kernelsquare.domainmysql.domain.member_authority.entity.MemberAuthority;
import com.kernelsquare.domainmysql.domain.member_authority.repository.MemberAuthorityRepository;
import com.kernelsquare.memberapi.domain.auth.dto.SignUpRequest;
import com.kernelsquare.memberapi.domain.auth.dto.SignUpResponse;

import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;
	private final MemberAuthorityRepository memberAuthorityRepository;
	private final AuthorityRepository authorityRepository;
	private final LevelRepository levelRepository;
	private final MemberReader memberReader;

	@LogExecutionTime
	@Transactional
	public MemberInfo login(final AuthCommand.LoginMember request) {
		var startTime = System.currentTimeMillis();
		Member findMember = memberReader.findMember(request.email());
		var executionTime = System.currentTimeMillis() - startTime;
		log.info("findMember executed in " + executionTime + "ms");

		startTime = System.currentTimeMillis();
//		if (!passwordEncoder.matches(loginRequest.password(), findMember.getPassword())) {
//			throw new BusinessException(AuthErrorCode.INVALID_PASSWORD);
//		}
		AuthValidation.validatePassword(request.password(), findMember.getPassword(), passwordEncoder);
		executionTime = System.currentTimeMillis() - startTime;
		log.info("addExperience executed in " + executionTime + "ms");

//		findMember.addExperience(ExperiencePolicy.MEMBER_DAILY_ATTENDED.getReward());
//		if (findMember.isExperienceExceed(findMember.getExperience())) {
//			findMember.updateExperience(findMember.getExperience() - findMember.getLevel().getLevelUpperLimit());
//			Level nextLevel = levelRepository.findByName(findMember.getLevel().getName() + 1)
//				.orElseThrow(() -> new BusinessException(LevelErrorCode.LEVEL_NOT_FOUND));
//			findMember.updateLevel(nextLevel);
//		}


		startTime = System.currentTimeMillis();
		addExperience(findMember);
		executionTime = System.currentTimeMillis() - startTime;
		log.info("addExperience executed in " + executionTime + "ms");

		return MemberInfo.from(findMember);
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

	private void addExperience(Member member) {
		member.addExperience(ExperiencePolicy.MEMBER_DAILY_ATTENDED.getReward());
		if (member.isExperienceExceed(member.getExperience())) {
			member.updateExperience(member.getExperience() - member.getLevel().getLevelUpperLimit());
			Level nextLevel = levelRepository.findByName(member.getLevel().getName() + 1)
				.orElseThrow(() -> new BusinessException(LevelErrorCode.LEVEL_NOT_FOUND));
			member.updateLevel(nextLevel);
		}
	}
}

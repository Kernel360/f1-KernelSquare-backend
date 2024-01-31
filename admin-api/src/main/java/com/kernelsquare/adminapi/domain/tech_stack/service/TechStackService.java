package com.kernelsquare.adminapi.domain.tech_stack.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kernelsquare.adminapi.domain.tech_stack.dto.CreateTechStackRequest;
import com.kernelsquare.adminapi.domain.tech_stack.dto.CreateTechStackResponse;
import com.kernelsquare.adminapi.domain.tech_stack.dto.FindAllTechStacksResponse;
import com.kernelsquare.adminapi.domain.tech_stack.dto.UpdateTechStackRequest;
import com.kernelsquare.core.common_response.error.code.TechStackErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.tech_stack.entity.TechStack;
import com.kernelsquare.domainmysql.domain.tech_stack.repository.TechStackRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TechStackService {
	private final TechStackRepository techStackRepository;

	public CreateTechStackResponse createTechStack(CreateTechStackRequest createTechStackRequest) {
		TechStack techStack = CreateTechStackRequest.toEntity(createTechStackRequest);
		try {
			TechStack saveTechStack = techStackRepository.save(techStack);

			return CreateTechStackResponse.from(saveTechStack);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(TechStackErrorCode.TECH_STACK_ALREADY_EXISTED);
		}
	}

	@Transactional(readOnly = true)
	public FindAllTechStacksResponse findAllTechStacks() {
		List<TechStack> techStackList = techStackRepository.findAll();

		return FindAllTechStacksResponse.from(techStackList);
	}

	@Transactional
	public void updateTechStack(Long techStackId, UpdateTechStackRequest updateTechStackRequest) {
		TechStack techStack = techStackRepository.findById(techStackId)
			.orElseThrow(() -> new BusinessException(TechStackErrorCode.TECH_STACK_NOT_FOUND));

		boolean isSkillExists = techStackRepository.existsBySkill(updateTechStackRequest.skill());

		if (isSkillExists) {
			throw new BusinessException(TechStackErrorCode.TECH_STACK_ALREADY_EXISTED);
		}

		techStack.update(updateTechStackRequest.skill());
	}

	@Transactional
	public void deleteTechStack(Long techStackId) {
		techStackRepository.deleteById(techStackId);
	}
}

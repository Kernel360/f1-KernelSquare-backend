package com.kernel360.kernelsquare.domain.tech_stack.service;

import com.kernel360.kernelsquare.domain.tech_stack.dto.CreateTechStackRequest;
import com.kernel360.kernelsquare.domain.tech_stack.dto.CreateTechStackResponse;
import com.kernel360.kernelsquare.domain.tech_stack.dto.FindAllTechStacksResponse;
import com.kernel360.kernelsquare.domain.tech_stack.entity.TechStack;
import com.kernel360.kernelsquare.domain.tech_stack.repository.TechStackRepository;
import com.kernel360.kernelsquare.global.common_response.error.code.TechStackErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TechStackService {
    private final TechStackRepository techStackRepository;

    @Transactional
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

        List<String> skills = techStackList.stream().map(TechStack::getSkill).toList();

        return FindAllTechStacksResponse.from(skills);
    }
}

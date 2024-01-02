package com.kernel360.kernelsquare.domain.tech_stack.service;

import com.kernel360.kernelsquare.domain.tech_stack.dto.CreateTechStackRequest;
import com.kernel360.kernelsquare.domain.tech_stack.dto.CreateTechStackResponse;
import com.kernel360.kernelsquare.domain.tech_stack.entity.TechStack;
import com.kernel360.kernelsquare.domain.tech_stack.repository.TechStackRepository;
import com.kernel360.kernelsquare.global.common_response.error.code.TechStackErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TechStackService {
    private final TechStackRepository techStackRepository;

    @Transactional
    public CreateTechStackResponse createTechStack(CreateTechStackRequest createTechStackRequest) {
        TechStack techStack = CreateTechStackRequest.toEntity(createTechStackRequest);
        try {
            techStackRepository.save(techStack);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(TechStackErrorCode.TECH_STACK_ALREADY_EXISTED);
        }
        return CreateTechStackResponse.of(techStack);
    }

    @Transactional
    public void deleteTechStack(Long techStackId) {
        techStackRepository.deleteById(techStackId);
    }
}

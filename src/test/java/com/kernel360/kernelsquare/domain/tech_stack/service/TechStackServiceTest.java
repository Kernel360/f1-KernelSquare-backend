package com.kernel360.kernelsquare.domain.tech_stack.service;

import com.kernel360.kernelsquare.domain.tech_stack.dto.CreateTechStackRequest;
import com.kernel360.kernelsquare.domain.tech_stack.dto.CreateTechStackResponse;
import com.kernel360.kernelsquare.domain.tech_stack.entity.TechStack;
import com.kernel360.kernelsquare.domain.tech_stack.repository.TechStackRepository;
import com.kernel360.kernelsquare.global.common_response.error.code.TechStackErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("기술 스택 서비스 통합 테스트")
@Transactional
@SpringBootTest
class TechStackServiceTest {
    @Autowired
    private TechStackService techStackService;
    @Autowired
    private TechStackRepository techStackRepository;

    @Test
    @DisplayName("기술 스택 생성 테스트")
    void testCreateTechStack() {
        //given
        String skill = "Java";
        CreateTechStackRequest createTechStackRequest = new CreateTechStackRequest(skill);

        //when
        CreateTechStackResponse createTechStackResponse = techStackService.createTechStack(createTechStackRequest);
        TechStack newTechStack = techStackRepository.findById(createTechStackResponse.skillId())
            .orElseThrow(() -> new BusinessException(TechStackErrorCode.TECH_STACK_NOT_FOUND));

        // then
        assertThat(newTechStack).isNotNull();
        assertThat(newTechStack.getSkill()).isEqualTo(skill);
    }
}
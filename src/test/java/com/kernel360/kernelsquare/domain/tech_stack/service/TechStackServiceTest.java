package com.kernel360.kernelsquare.domain.tech_stack.service;

import com.kernel360.kernelsquare.domain.tech_stack.dto.CreateTechStackRequest;
import com.kernel360.kernelsquare.domain.tech_stack.entity.TechStack;
import com.kernel360.kernelsquare.domain.tech_stack.repository.TechStackRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("기술 스택 서비스 통합 테스트")
@SpringBootTest
class TechStackServiceTest {
    @InjectMocks
    private TechStackService techStackService;
    @Mock
    private TechStackRepository techStackRepository;

    @Test
    @DisplayName("기술 스택 생성 테스트")
    void testCreateTechStack() {
        //given
        String skill = "Java";
        CreateTechStackRequest createTechStackRequest = new CreateTechStackRequest(skill);
        TechStack techStack = CreateTechStackRequest.toEntity(createTechStackRequest);

        given(techStackRepository.save(any(TechStack.class))).willReturn(techStack);

        //when
        techStackService.createTechStack(createTechStackRequest);

        // then
        verify(techStackRepository, times(1)).save(any(TechStack.class));
    }

    @Test
    @DisplayName("기술 스택 모든 조회 테스트")
    void testFindAllTechStacks() {
        //given
        List<TechStack> techStackList = Arrays.asList(
            new TechStack("JavaScript"),
            new TechStack("Python")
        );

        given(techStackRepository.findAll()).willReturn(techStackList);

        //when
        techStackService.findAllTechStacks();

        //then
        verify(techStackRepository,times(1)).findAll();
    }
}
package com.kernel360.kernelsquare.domain.tech_stack.service;

import com.kernel360.kernelsquare.domain.tech_stack.dto.CreateTechStackRequest;
import com.kernel360.kernelsquare.domain.tech_stack.dto.CreateTechStackResponse;
import com.kernel360.kernelsquare.domain.tech_stack.dto.FindAllTechStacksResponse;
import com.kernel360.kernelsquare.domain.tech_stack.entity.TechStack;
import com.kernel360.kernelsquare.domain.tech_stack.repository.TechStackRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("기술 스택 서비스 통합 테스트")
@ExtendWith(MockitoExtension.class)
class TechStackServiceTest {
    @InjectMocks
    private TechStackService techStackService;
    @Mock
    private TechStackRepository techStackRepository;

    @Test
    @DisplayName("기술 스택 생성 테스트")
    void testCreateTechStack() {
        //given
        TechStack techStack = new TechStack(1L, "DB");
        CreateTechStackRequest createTechStackRequest = new CreateTechStackRequest(techStack.getSkill());

        given(techStackRepository.save(any(TechStack.class))).willReturn(techStack);

        //when
        CreateTechStackResponse createTechStackResponse = techStackService.createTechStack(createTechStackRequest);

        //then
        assertThat(createTechStackResponse).isNotNull();
        assertThat(createTechStackResponse.skillId()).isEqualTo(techStack.getId());

        //verify
        verify(techStackRepository, times(1)).save(any(TechStack.class));
    }

    @Test
    @DisplayName("기술 스택 모든 조회 테스트")
    void testFindAllTechStacks() {
        //given
        List<TechStack> techStackList = Arrays.asList(
            new TechStack(1L, "JavaScript"),
            new TechStack(2L, "Python")
        );

        given(techStackRepository.findAll()).willReturn(techStackList);

        //when
        FindAllTechStacksResponse findAllTechStacksResponse = techStackService.findAllTechStacks();

        //then
        assertThat(findAllTechStacksResponse).isNotNull();
        assertThat(findAllTechStacksResponse.skills()).isEqualTo(techStackList.stream().map(TechStack::getSkill).toList());

        //verify
        verify(techStackRepository,times(1)).findAll();
    }
}
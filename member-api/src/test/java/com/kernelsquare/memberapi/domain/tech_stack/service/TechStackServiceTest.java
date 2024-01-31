package com.kernelsquare.memberapi.domain.tech_stack.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kernelsquare.memberapi.domain.tech_stack.dto.FindAllTechStacksResponse;
import com.kernelsquare.domainmysql.domain.tech_stack.entity.TechStack;
import com.kernelsquare.domainmysql.domain.tech_stack.repository.TechStackRepository;

@DisplayName("기술 스택 서비스 통합 테스트")
@ExtendWith(MockitoExtension.class)
class TechStackServiceTest {
	@InjectMocks
	private TechStackService techStackService;
	@Mock
	private TechStackRepository techStackRepository;

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
		assertThat(findAllTechStacksResponse.skills()).isEqualTo(techStackList);

		//verify
		verify(techStackRepository, times(1)).findAll();
	}
}
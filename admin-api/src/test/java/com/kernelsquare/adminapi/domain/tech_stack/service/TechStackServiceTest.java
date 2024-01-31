package com.kernelsquare.adminapi.domain.tech_stack.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kernelsquare.adminapi.domain.tech_stack.dto.CreateTechStackRequest;
import com.kernelsquare.adminapi.domain.tech_stack.dto.CreateTechStackResponse;
import com.kernelsquare.adminapi.domain.tech_stack.dto.FindAllTechStacksResponse;
import com.kernelsquare.adminapi.domain.tech_stack.dto.UpdateTechStackRequest;
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
		assertThat(findAllTechStacksResponse.skills()).isEqualTo(techStackList);

		//verify
		verify(techStackRepository, times(1)).findAll();
	}

	@Test
	@DisplayName("기술 스택 수정 테스트")
	void testUpdateTechStack() {
		//given
		TechStack techStack = new TechStack(1L, "Spring");

		given(techStackRepository.findById(anyLong())).willReturn(Optional.of(techStack));

		UpdateTechStackRequest updateTechStackRequest = new UpdateTechStackRequest("Django");

		//when
		techStackService.updateTechStack(1L, updateTechStackRequest);

		//then
		assertThat(techStack).isNotNull();
		assertThat(techStack.getSkill()).isEqualTo(updateTechStackRequest.skill());

		//verify
		verify(techStackRepository, times(1)).findById(anyLong());
	}

	@Test
	@DisplayName("기술 스택 삭제 테스트")
	void testDeleteTechStack() {
		//given
		TechStack techStack = new TechStack(1L, "HTTP");

		doNothing()
			.when(techStackRepository)
			.deleteById(anyLong());

		//when
		techStackService.deleteTechStack(techStack.getId());

		//then
		verify(techStackRepository, times(1)).deleteById(anyLong());
	}
}
package com.kernelsquare.adminapi.domain.tech_stack.controller;

import static com.kernelsquare.core.common_response.response.code.TechStackResponseCode.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.anyLong;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kernelsquare.adminapi.domain.tech_stack.dto.CreateTechStackRequest;
import com.kernelsquare.adminapi.domain.tech_stack.dto.CreateTechStackResponse;
import com.kernelsquare.adminapi.domain.tech_stack.dto.FindAllTechStacksResponse;
import com.kernelsquare.adminapi.domain.tech_stack.dto.UpdateTechStackRequest;
import com.kernelsquare.adminapi.domain.tech_stack.service.TechStackService;
import com.kernelsquare.domainmysql.domain.tech_stack.entity.TechStack;

@DisplayName("기술 스택 컨트롤러 통합 테스트")
@WithMockUser
@WebMvcTest(TechStackController.class)
class TechStackControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private TechStackService techStackService;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Test
	@DisplayName("기술 스택 생성 성공시 200 OK와 응답 메시지를 반환한다")
	void testCreateTechStack() throws Exception {
		//given
		TechStack techStack = new TechStack(1L, "Java");
		CreateTechStackRequest createTechStackRequest = new CreateTechStackRequest(techStack.getSkill());
		CreateTechStackResponse createTechStackResponse = CreateTechStackResponse.from(techStack);

		given(techStackService.createTechStack(any(CreateTechStackRequest.class))).willReturn(createTechStackResponse);

		String jsonRequest = objectMapper.writeValueAsString(createTechStackRequest);

		//when & then
		mockMvc.perform(post("/api/v1/techs")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest))
			.andExpect(status().is(TECH_STACK_CREATED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(TECH_STACK_CREATED.getCode()))
			.andExpect(jsonPath("$.msg").value(TECH_STACK_CREATED.getMsg()))
			.andExpect(jsonPath("$.data.skill_id").value(techStack.getId()));

		//verify
		verify(techStackService, times(1)).createTechStack(any(CreateTechStackRequest.class));
	}

	@Test
	@DisplayName("기술 스택 모든 조회 성공시 200 OK와 응답 메시지를 반환한다.")
	void testFindAllTechStacks() throws Exception {
		//given
		TechStack techStack1 = new TechStack(1L, "JavaScript");
		TechStack techStack2 = new TechStack(2L, "Python");

		List<TechStack> testSkills = List.of(
			techStack1,
			techStack2
		);

		FindAllTechStacksResponse findAllTechStacksResponse = FindAllTechStacksResponse.from(testSkills);

		given(techStackService.findAllTechStacks()).willReturn(findAllTechStacksResponse);

		//when & then
		mockMvc.perform(get("/api/v1/techs")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"))
			.andExpect(status().is(TECH_STACK_ALL_FOUND.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(TECH_STACK_ALL_FOUND.getCode()))
			.andExpect(jsonPath("$.msg").value(TECH_STACK_ALL_FOUND.getMsg()))
			.andExpect(jsonPath("$.data.skills[0].id").value(testSkills.get(0).getId()))
			.andExpect(jsonPath("$.data.skills[1].id").value(testSkills.get(1).getId()));

		//verify
		verify(techStackService, times(1)).findAllTechStacks();
	}

	@Test
	@DisplayName("기술 스택 수정 성공시 200 Ok와 응답 메시지를 반환한다.")
	void testUpdateTechStack() throws Exception {
		//given
		TechStack techStack = new TechStack(1L, "Spring");

		UpdateTechStackRequest updateTechStackRequest = new UpdateTechStackRequest("Django");

		doNothing()
			.when(techStackService)
			.updateTechStack(anyLong(), any(UpdateTechStackRequest.class));

		String jsonRequest = objectMapper.writeValueAsString(updateTechStackRequest);

		//when & then
		mockMvc.perform(put("/api/v1/techs/" + techStack.getId())
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest))
			.andExpect(status().is(TECH_STACK_UPDATED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(TECH_STACK_UPDATED.getCode()))
			.andExpect(jsonPath("$.msg").value(TECH_STACK_UPDATED.getMsg()));

		//verify
		verify(techStackService, times(1)).updateTechStack(anyLong(), any(UpdateTechStackRequest.class));
	}

	@Test
	@DisplayName("기술 스택 삭제 성공시 200 OK와 응답 메시지를 반환한다.")
	void testDeleteTechStack() throws Exception {
		//given
		TechStack techStack = new TechStack(1L, "HTTP");

		doNothing()
			.when(techStackService)
			.deleteTechStack(anyLong());

		//when & then
		mockMvc.perform(delete("/api/v1/techs/" + techStack.getId())
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"))
			.andExpect(status().is(TECH_STACK_DELETED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(TECH_STACK_DELETED.getCode()))
			.andExpect(jsonPath("$.msg").value(TECH_STACK_DELETED.getMsg()));

		//verify
		verify(techStackService, times(1)).deleteTechStack(anyLong());
	}
}
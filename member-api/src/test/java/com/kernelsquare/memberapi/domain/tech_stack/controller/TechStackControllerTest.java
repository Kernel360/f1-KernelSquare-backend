package com.kernelsquare.memberapi.domain.tech_stack.controller;

import static com.kernelsquare.core.common_response.response.code.TechStackResponseCode.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import com.kernelsquare.core.dto.PageResponse;
import com.kernelsquare.core.dto.Pagination;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kernelsquare.memberapi.domain.tech_stack.dto.FindAllTechStacksResponse;
import com.kernelsquare.memberapi.domain.tech_stack.service.TechStackService;
import com.kernelsquare.domainmysql.domain.tech_stack.entity.TechStack;

@DisplayName("기술 스택 컨트롤러 단위 테스트")
@WithMockUser
@WebMvcTest(TechStackController.class)
class TechStackControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private TechStackService techStackService;
	private ObjectMapper objectMapper = new ObjectMapper();

	@Test
	@DisplayName("기술 스택 모든 조회 성공시 200 OK와 응답 메시지를 반환한다.")
	void testFindAllTechStacks() throws Exception {
		//given
		TechStack techStack1 = new TechStack(1L, "JavaScript");
		TechStack techStack2 = new TechStack(2L, "Python");

		Pageable pageable = PageRequest.of(0, 2);
		Pagination pagination = Pagination.builder()
			.totalPage(1)
			.pageable(pageable.getPageSize())
			.isEnd(true)
			.build();

		List<TechStack> testSkills = List.of(
			techStack1,
			techStack2
		);

		List<String> responsePages = testSkills.stream()
			.map(TechStack::getSkill)
			.toList();

		PageResponse<String> response = PageResponse.of(pagination, responsePages);

		given(techStackService.findAllTechStacks(any(Pageable.class))).willReturn(response);

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
			.andExpect(jsonPath("$.data.pagination.total_page").value(1))
			.andExpect(jsonPath("$.data.pagination.pageable").value(pageable.getPageSize()))
			.andExpect(jsonPath("$.data.pagination.is_end").value(true));

		//verify
		verify(techStackService, times(1)).findAllTechStacks(any(Pageable.class));
	}
}
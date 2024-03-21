package com.kernelsquare.memberapi.domain.tech_stack.controller;

import static com.kernelsquare.core.common_response.response.code.TechStackResponseCode.*;
import static com.kernelsquare.memberapi.config.ApiDocumentUtils.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kernelsquare.memberapi.config.RestDocsControllerTest;
import com.kernelsquare.memberapi.domain.tech_stack.dto.FindAllTechStacksResponse;
import com.kernelsquare.memberapi.domain.tech_stack.service.TechStackService;
import com.kernelsquare.domainmysql.domain.tech_stack.entity.TechStack;

@DisplayName("기술 스택 컨트롤러 단위 테스트")
@WithMockUser
@WebMvcTest(TechStackController.class)
class TechStackControllerTest extends RestDocsControllerTest {
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

		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/techs")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"));

		//then
		resultActions
			.andExpect(status().is(TECH_STACK_ALL_FOUND.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("tech-stack-all-found", getDocumentRequest(), getDocumentResponse(),
				responseFields(
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
					fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("data.pagination.total_page").type(JsonFieldType.NUMBER)
						.description("검색된 기술 스택의 총 페이지 수"),
					fieldWithPath("data.pagination.pageable").type(JsonFieldType.NUMBER)
						.description("한 페이지에 보여질 기술 스택의 개수"),
					fieldWithPath("data.pagination.is_end").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
					fieldWithPath("data.list").type(JsonFieldType.ARRAY).description("기술 스택 목록")
				)));

		//verify
		verify(techStackService, times(1)).findAllTechStacks(any(Pageable.class));
	}
}
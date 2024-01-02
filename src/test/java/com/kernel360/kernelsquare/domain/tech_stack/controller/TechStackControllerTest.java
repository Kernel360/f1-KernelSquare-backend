package com.kernel360.kernelsquare.domain.tech_stack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kernel360.kernelsquare.domain.tech_stack.dto.CreateTechStackRequest;
import com.kernel360.kernelsquare.domain.tech_stack.dto.CreateTechStackResponse;
import com.kernel360.kernelsquare.domain.tech_stack.dto.FindAllTechStacksResponse;
import com.kernel360.kernelsquare.domain.tech_stack.entity.TechStack;
import com.kernel360.kernelsquare.domain.tech_stack.service.TechStackService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.kernel360.kernelsquare.global.common_response.response.code.TechStackResponseCode.TECH_STACK_ALL_FOUND;
import static com.kernel360.kernelsquare.global.common_response.response.code.TechStackResponseCode.TECH_STACK_CREATED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

        List<String> testSkills = List.of(
            techStack1.getSkill(),
            techStack2.getSkill()
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
        .andExpect(jsonPath("$.data.skills[0]").value(testSkills.get(0)))
        .andExpect(jsonPath("$.data.skills[1]").value(testSkills.get(1)));

        //verify
        verify(techStackService, times(1)).findAllTechStacks();
    }
}
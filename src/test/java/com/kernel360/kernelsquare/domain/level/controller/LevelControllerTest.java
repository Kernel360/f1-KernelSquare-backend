package com.kernel360.kernelsquare.domain.level.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kernel360.kernelsquare.domain.level.dto.CreateLevelRequest;
import com.kernel360.kernelsquare.domain.level.dto.CreateLevelResponse;
import com.kernel360.kernelsquare.domain.level.dto.FindAllLevelResponse;
import com.kernel360.kernelsquare.domain.level.dto.LevelDto;
import com.kernel360.kernelsquare.domain.level.entity.Level;
import com.kernel360.kernelsquare.domain.level.service.LevelService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static com.kernel360.kernelsquare.global.common_response.response.code.LevelResponseCode.LEVEL_CREATED;
import static com.kernel360.kernelsquare.global.common_response.response.code.LevelResponseCode.LEVEL_FOUND;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("등급 컨트롤러 통합 테스트")
@WithMockUser
@WebMvcTest(LevelController.class)
class LevelControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LevelService levelService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("등급 생성 성공시 200 OK와 응답 메시지를 반환한다")
    void testCreateLevel() throws Exception {

        // Given
        Long name = 3L;
        String imageUrl = "test22";

        Level level = new Level(name, imageUrl);
        CreateLevelRequest createLevelRequest = new CreateLevelRequest(name, imageUrl);
        CreateLevelResponse createLevelResponse = CreateLevelResponse.from(level);

        doReturn(createLevelResponse)
                .when(levelService)
                .createLevel(any(CreateLevelRequest.class));

        String jsonRequest = objectMapper.writeValueAsString(createLevelRequest);

        // When & Then
        mockMvc.perform(post("/api/v1/levels")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonRequest))
                .andExpect(status().is(LEVEL_CREATED.getStatus().value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(LEVEL_CREATED.getCode()))
                .andExpect(jsonPath("$.msg").value(LEVEL_CREATED.getMsg()))
                .andExpect(jsonPath("$.data.level_id").value(level.getId()));

        // Verify
        verify(levelService, times(1)).createLevel(any(CreateLevelRequest.class));


    }


    @Test
    @DisplayName("모든 레벨 조회 성공시 200 OK와 레벨 목록을 반환한다")
    void testFindAllLevel() throws Exception {
        // given
        List<Level> levelList = Arrays.asList(
                Level.builder().name(1L).imageUrl("image1.jpg").build(),
                Level.builder().name(2L).imageUrl("image2.jpg").build()
        );
        FindAllLevelResponse response = FindAllLevelResponse.from(levelList);

        doReturn(response)
                .when(levelService)
                .findAllLevel();

        // when & then
        mockMvc.perform(get("/api/v1/levels")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().is(LEVEL_FOUND.getStatus().value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(LEVEL_FOUND.getCode()))
                .andExpect(jsonPath("$.msg").value(LEVEL_FOUND.getMsg()))
                .andExpect(jsonPath("$.data.levels[0].image_url").value(levelList.get(0).getImageUrl()));
//                .andExpect(jsonPath("$.data.levels[1]").value(levelList.get(1)));

        // verify
        verify(levelService, times(1)).findAllLevel();
    }

}
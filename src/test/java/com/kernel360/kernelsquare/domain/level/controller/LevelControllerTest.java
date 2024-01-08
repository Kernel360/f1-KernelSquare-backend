package com.kernel360.kernelsquare.domain.level.controller;

import static com.kernel360.kernelsquare.global.common_response.response.code.LevelResponseCode.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
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
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.kernel360.kernelsquare.domain.level.dto.CreateLevelRequest;
import com.kernel360.kernelsquare.domain.level.dto.CreateLevelResponse;
import com.kernel360.kernelsquare.domain.level.dto.FindAllLevelResponse;
import com.kernel360.kernelsquare.domain.level.dto.UpdateLevelRequest;
import com.kernel360.kernelsquare.domain.level.dto.UpdateLevelResponse;
import com.kernel360.kernelsquare.domain.level.entity.Level;
import com.kernel360.kernelsquare.domain.level.service.LevelService;

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

		Level level = Level.builder()
			.id(1L)
			.name(name)
			.imageUrl(imageUrl)
			.build();
		CreateLevelRequest createLevelRequest = new CreateLevelRequest(name, imageUrl);
		CreateLevelResponse createLevelResponse = CreateLevelResponse.from(level);

		doReturn(createLevelResponse)
			.when(levelService)
			.createLevel(any(CreateLevelRequest.class));

		objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		String jsonRequest = objectMapper.writeValueAsString(createLevelRequest);

		// When & Then
		mockMvc.perform(post("/api/v1/levels")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(status().is(LEVEL_CREATED.getStatus().value()))
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
			.andExpect(jsonPath("$.msg").value(LEVEL_FOUND.getMsg()));

		// verify
		verify(levelService, times(1)).findAllLevel();
	}

	@Test
	@DisplayName("레벨 삭제 성공 시 200 OK와 메시지를 반환한다")
	void testDeleteLevel() throws Exception {
		// Given
		Level level = new Level(1L, 11L, "image9.jpg");

		doNothing()
			.when(levelService)
			.deleteLevel(anyLong());

		// When & Then
		mockMvc.perform(delete("/api/v1/levels/" + level.getId())
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8"))
			.andExpect(status().is(LEVEL_DELETED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(LEVEL_DELETED.getCode()))
			.andExpect(jsonPath("$.msg").value(LEVEL_DELETED.getMsg()));

		// Verify
		verify(levelService, times(1)).deleteLevel(anyLong());
	}

	@Test
	@DisplayName("레벨 수정 성공 시 200 OK와 메시지를 반환한다")
	void testUpdateLevel() throws Exception {
		// Given
		Long id = 1L;
		Long name = 3L;
		String imageUrl = "image1.jpg";

		Level level = Level.builder()
			.id(id)
			.name(name)
			.imageUrl(imageUrl)
			.build();
		UpdateLevelRequest updateLevelRequest = new UpdateLevelRequest(id, name, imageUrl);
		UpdateLevelResponse updateLevelResponse = UpdateLevelResponse.from(level);

		doReturn(updateLevelResponse)
			.when(levelService)
			.updateLevel(anyLong(), any(UpdateLevelRequest.class));

		String jsonRequest = objectMapper.writeValueAsString(updateLevelRequest);

		// When & Then
		mockMvc.perform(put("/api/v1/levels/" + level.getId())
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest))
			.andExpect(status().is(LEVEL_UPDATED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.code").value(LEVEL_UPDATED.getCode()))
			.andExpect(jsonPath("$.msg").value(LEVEL_UPDATED.getMsg()));

		// Verify
		verify(levelService, times(1)).updateLevel(anyLong(), any(UpdateLevelRequest.class));
	}

}

package com.kernel360.kernelsquare.domain.level.service;

import com.kernel360.kernelsquare.domain.level.dto.*;
import com.kernel360.kernelsquare.domain.level.entity.Level;
import com.kernel360.kernelsquare.domain.level.repository.LevelRepository;
import com.kernel360.kernelsquare.global.common_response.error.code.LevelErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;



import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("등급 서비스 통합 테스트")
@ExtendWith(MockitoExtension.class)
class LevelServiceTest {
    @InjectMocks
    private LevelService levelService;
    @Mock
    private LevelRepository levelRepository;

    @Test
    @DisplayName("등급 생성 테스트")
    void testCreateLevel() {
        // Given
        CreateLevelRequest createLevelRequest = new CreateLevelRequest(3L, "testurl");

        Level level = CreateLevelRequest.toEntity(createLevelRequest);

        given(levelRepository.save(any(Level.class))).willReturn(level);

        // When
        CreateLevelResponse createLevelResponse = levelService.createLevel(createLevelRequest);

        // Then
        assertThat(createLevelResponse).isNotNull();
        assertThat(createLevelResponse.levelId()).isEqualTo(level.getId());
    }

    @Test
    @DisplayName("등급 조회 테스트")
    void testFindAllLevel() {
        // given
        List<Level> expectedLevels = Arrays.asList(
                Level.builder().name(1L).imageUrl("image1.jpg").build(),
                Level.builder().name(2L).imageUrl("image2.jpg").build()
        );
        given(levelRepository.findAll()).willReturn(expectedLevels);

        // when
        FindAllLevelResponse actualLevels = levelService.findAllLevel();

        // then
        assertThat(actualLevels.levels().get(0).id()).isEqualTo(expectedLevels.get(0).getId());
        assertThat(actualLevels.levels().get(0).name()).isEqualTo(expectedLevels.get(0).getName());
        assertThat(actualLevels.levels().get(1).imageUrl()).isEqualTo("null/" + expectedLevels.get(1).getImageUrl());

        verify(levelRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("등급 삭제 테스트")
    void testDeleteLevel() {
        // Given
        Level level = new Level(1L, 11L, "image1.jpg");

        doNothing().when(levelRepository).deleteById(level.getId());
        // When
        levelService.deleteLevel(level.getId());

        // Then
        verify(levelRepository).deleteById(level.getId());
    }

    @Test
    @DisplayName("등급 수정 테스트")
    void testUpdateLevel() {
        // Given
        Level level = new Level(1L, 1L, "image1.jpg");
        UpdateLevelRequest updateLevelRequest = new UpdateLevelRequest(1L, 2L, "image2.jpg");
        given(levelRepository.findById(anyLong())).willReturn(Optional.of(level));

        // When
        levelService.updateLevel(1L,updateLevelRequest);

        // Then
        assertThat(level).isNotNull();
        assertThat(level.getName()).isEqualTo(updateLevelRequest.name());
        assertThat(level.getImageUrl()).isEqualTo(updateLevelRequest.imageUrl());

        // Verify
        verify(levelRepository, times(1)).findById(anyLong());

    }

}
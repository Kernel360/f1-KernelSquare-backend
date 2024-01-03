package com.kernel360.kernelsquare.domain.level.service;

import com.kernel360.kernelsquare.domain.level.dto.CreateLevelRequest;
import com.kernel360.kernelsquare.domain.level.dto.CreateLevelResponse;
import com.kernel360.kernelsquare.domain.level.dto.FindAllLevelResponse;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
                new Level(1L, "image1.jpg"),
                new Level(2L, "image2.jpg")
        );
        given(levelRepository.findAll()).willReturn(expectedLevels);

        // when
        FindAllLevelResponse actualLevels = levelService.findAllLevel();

        // then
        assertThat(actualLevels.levels().get(0).id()).isEqualTo(expectedLevels.get(0).getId());
        assertThat(actualLevels.levels().get(0).name()).isEqualTo(expectedLevels.get(0).getName());
        assertThat(actualLevels.levels().get(1).imageUrl()).isEqualTo(expectedLevels.get(1).getImageUrl());

        verify(levelRepository, times(1)).findAll();
    }

}
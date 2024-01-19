package com.kernel360.kernelsquare.domain.level.service;

import com.kernel360.kernelsquare.domain.level.dto.CreateLevelRequest;
import com.kernel360.kernelsquare.domain.level.dto.CreateLevelResponse;
import com.kernel360.kernelsquare.domain.level.dto.FindAllLevelResponse;
import com.kernel360.kernelsquare.domain.level.dto.UpdateLevelRequest;
import com.kernel360.kernelsquare.domain.level.entity.Level;
import com.kernel360.kernelsquare.domain.level.repository.LevelRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

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

    @Value("${custom.domain.image.baseUrl}")
    private String baseUrl;

    @Test
    @DisplayName("등급 생성 테스트")
    void testCreateLevel() {
        // Given
        CreateLevelRequest createLevelRequest = new CreateLevelRequest(3L, "testurl", 2000L);

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
        assertThat(actualLevels.levels().get(1).imageUrl()).isEqualTo(baseUrl + "/" + expectedLevels.get(1).getImageUrl());

        verify(levelRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("등급 삭제 테스트")
    void testDeleteLevel() {
        // Given
        Level level = Level.builder()
            .id(1L)
            .name(11L)
            .imageUrl("image9.jpg")
            .build();

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
        Level level = Level.builder()
            .id(1L)
            .name(1L)
            .imageUrl("image1.jpg")
            .build();
        UpdateLevelRequest updateLevelRequest = new UpdateLevelRequest(2L, "image2.jpg",1100L);
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
package com.kernel360.kernelsquare.domain.level.service;

import com.kernel360.kernelsquare.domain.level.dto.CreateLevelRequest;
import com.kernel360.kernelsquare.domain.level.dto.CreateLevelResponse;
import com.kernel360.kernelsquare.domain.level.entity.Level;
import com.kernel360.kernelsquare.domain.level.repository.LevelRepository;
import com.kernel360.kernelsquare.global.common_response.error.code.LevelErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.plaf.ViewportUI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("등급 서비스 통합 테스트")
@Transactional
@SpringBootTest
class LevelServiceTest {
    @Autowired
    private LevelService levelService;
    @Autowired
    private LevelRepository levelRepository;

    @Test
    @DisplayName("등급 생성 테스트")
    void testCreateLevel() {
        // Given
        Long name = 3L;
        String imageUrl = "test22";
        CreateLevelRequest createLevelRequest = new CreateLevelRequest(name, imageUrl);

        // When
        CreateLevelResponse createLevelResponse = levelService.createLevel(createLevelRequest);
        Level newLevel = levelRepository.findById(createLevelResponse.levelId())
                .orElseThrow(() -> new BusinessException(LevelErrorCode.LEVEL_NOT_FOUND));

        // Then
        assertThat(newLevel).isNotNull();
        assertThat(newLevel.getName()).isEqualTo(name);
    }


}
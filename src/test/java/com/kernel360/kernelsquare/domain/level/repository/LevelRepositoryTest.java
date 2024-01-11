package com.kernel360.kernelsquare.domain.level.repository;

import com.kernel360.kernelsquare.domain.level.entity.Level;
import com.kernel360.kernelsquare.global.common_response.error.code.LevelErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;
import com.kernel360.kernelsquare.global.config.JpaAuditingConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("레벨 레포지토리 통합 테스트")
@DataJpaTest
@Import(JpaAuditingConfig.class)
class LevelRepositoryTest {
    @Autowired
    private LevelRepository levelRepository;

    @Test
    @DisplayName("레벨 findLevelByName 정상 작동 테스트")
    void testFindLevelByName() {
        //given
        Level level = Level.builder()
            .id(1L)
            .name(1L)
            .imageUrl("level/level1.png")
            .levelUpperLimit(200L)
            .build();

        levelRepository.save(level);

        //when
        Level findLevel = levelRepository.findLevelByName(level.getName())
            .orElseThrow(() -> new BusinessException(LevelErrorCode.LEVEL_NOT_FOUND));

        //then
        assertThat(findLevel.getId()).isEqualTo(level.getId());
        assertThat(findLevel.getName()).isEqualTo(level.getName());
        assertThat(findLevel.getImageUrl()).isEqualTo(level.getImageUrl());
        assertThat(findLevel.getLevelUpperLimit()).isEqualTo(level.getLevelUpperLimit());
    }

    @Test
    @DisplayName("레벨 findByNameAndIdNot 정상 작동 테스트")
    void testFindByNameAndIdNot() {
        //given
        Level level = Level.builder()
            .id(1L)
            .name(1L)
            .imageUrl("level/level1.png")
            .levelUpperLimit(200L)
            .build();

        levelRepository.save(level);

        //when
        Optional<Level> findLevel = levelRepository.findByNameAndIdNot(level.getName(), level.getId());

        //then
        assertThat(findLevel).isEmpty();
    }
}
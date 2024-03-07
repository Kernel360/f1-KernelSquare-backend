package com.kernelsquare.domainmysql.domain.level.repository;

import com.kernelsquare.core.common_response.error.code.LevelErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LevelReaderImpl implements LevelReader {
    private final LevelRepository levelRepository;

    @Override
    public Level findLevel(Long name) {
        return levelRepository.findByName(name)
            .orElseThrow(() -> new BusinessException(LevelErrorCode.LEVEL_NOT_FOUND));
    }

    @Override
    public Level findLevelOtherThanId(Long levelName, Long levelId) {
        return levelRepository.findByNameAndIdNot(levelName, levelId)
            .orElseThrow(() -> new BusinessException(LevelErrorCode.LEVEL_ALREADY_EXISTED));
    }
}

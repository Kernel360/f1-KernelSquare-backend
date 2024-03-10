package com.kernelsquare.domainmysql.domain.level.repository;

import com.kernelsquare.domainmysql.domain.level.entity.Level;

public interface LevelReader {
    Level findLevel(Long name);

    Level findLevelOtherThanId(Long levelName, Long levelId);
}

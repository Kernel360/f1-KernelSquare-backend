package com.kernel360.kernelsquare.domain.level.repository;

import com.kernel360.kernelsquare.domain.level.entity.Level;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LevelRepository extends JpaRepository<Level, Long> {
    List<Level> findAll();
}

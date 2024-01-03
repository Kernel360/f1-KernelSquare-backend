package com.kernel360.kernelsquare.domain.level.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kernel360.kernelsquare.domain.level.entity.Level;

public interface LevelRepository extends JpaRepository<Level, Long> {
	Optional<Level> findLevelByName(Long name);
  List<Level> findAll();
}

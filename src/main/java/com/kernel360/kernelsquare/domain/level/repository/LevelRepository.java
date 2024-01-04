package com.kernel360.kernelsquare.domain.level.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kernel360.kernelsquare.domain.level.entity.Level;

public interface LevelRepository extends JpaRepository<Level, Long> {
	Optional<Level> findLevelByName(Long name);

	List<Level> findAll();
}

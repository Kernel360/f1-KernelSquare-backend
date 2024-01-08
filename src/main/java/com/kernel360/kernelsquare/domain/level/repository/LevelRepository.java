package com.kernel360.kernelsquare.domain.level.repository;

import com.kernel360.kernelsquare.domain.level.entity.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LevelRepository extends JpaRepository<Level, Long> {
	Optional<Level> findLevelByName(Long name);

	@Query("SELECT l FROM level l WHERE l.name = :levelName AND l.id != :levelId")
	Optional<Level> findByNameAndIdNot(@Param("levelName") Long levelName, @Param("levelId") Long levelId);
}

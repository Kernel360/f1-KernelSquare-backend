package com.kernelsquare.domainmysql.domain.level.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kernelsquare.domainmysql.domain.level.entity.Level;

public interface LevelRepository extends JpaRepository<Level, Long> {
	Optional<Level> findByName(Long name);

	@Query("SELECT l FROM Level l WHERE l.name = :levelName AND l.id != :levelId")
	Optional<Level> findByNameAndIdNot(@Param("levelName") Long levelName, @Param("levelId") Long levelId);
}

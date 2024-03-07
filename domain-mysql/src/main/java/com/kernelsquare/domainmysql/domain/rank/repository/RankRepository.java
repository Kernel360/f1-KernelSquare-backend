package com.kernelsquare.domainmysql.domain.rank.repository;

import com.kernelsquare.domainmysql.domain.rank.entity.Rank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RankRepository extends JpaRepository<Rank, Long> {
    Optional<Rank> findByName(Long name);
}

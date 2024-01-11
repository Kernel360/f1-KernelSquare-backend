package com.kernel360.kernelsquare.domain.hashtag.repository;

import com.kernel360.kernelsquare.domain.hashtag.entity.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {
}

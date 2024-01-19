package com.kernel360.kernelsquare.domain.hashtag.repository;

import com.kernel360.kernelsquare.domain.hashtag.entity.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {
    @Modifying
    @Query("DELETE FROM Hashtag a WHERE a.reservationArticle.id = :postId")
    void deleteAllByReservationArticleIdInBatch(@Param("postId") Long postId);
}

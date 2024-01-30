package com.kernel360.kernelsquare.domain.hashtag.repository;

import com.kernel360.kernelsquare.domain.hashtag.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    @Modifying
    @Query("DELETE FROM Hashtag a WHERE a.reservationArticle.id = :postId")
    void deleteAllByReservationArticleId(@Param("postId") Long postId);

    List<Hashtag> findAllByReservationArticleId(Long articleId);

    Long countAllByReservationArticleId(Long articleId);
}

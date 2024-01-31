package com.kernelsquare.domainmysql.domain.hashtag.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kernelsquare.domainmysql.domain.hashtag.entity.Hashtag;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
	@Modifying
	@Query("DELETE FROM Hashtag a WHERE a.reservationArticle.id = :postId")
	void deleteAllByReservationArticleId(@Param("postId") Long postId);

	List<Hashtag> findAllByReservationArticleId(Long articleId);

	Long countAllByReservationArticleId(Long articleId);
}

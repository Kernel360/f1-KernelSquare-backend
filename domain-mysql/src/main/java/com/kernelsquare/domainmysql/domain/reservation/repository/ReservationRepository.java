package com.kernelsquare.domainmysql.domain.reservation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kernelsquare.domainmysql.domain.reservation.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	List<Reservation> findAllByReservationArticleId(Long articleId);

	Long countAllByReservationArticleId(Long articleId);

	Long countByReservationArticleIdAndMemberIdIsNull(Long articleId);

	@Modifying
	@Query("DELETE FROM Reservation a WHERE a.reservationArticle.id = :postId")
	void deleteAllByReservationArticleId(@Param("postId") Long postId);
}

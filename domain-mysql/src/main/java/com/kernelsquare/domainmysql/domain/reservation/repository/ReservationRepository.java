package com.kernelsquare.domainmysql.domain.reservation.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kernelsquare.domainmysql.domain.reservation.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	Boolean existsByReservationArticleIdAndMemberId(Long reservationArticleId, Long memberId);

	Boolean existsByMemberIdAndEndTimeAfter(Long memberId, LocalDateTime currentTime);

	List<Reservation> findAllByReservationArticleId(Long articleId);

	List<Reservation> findAllByMemberId(Long memberId);

	Long countAllByReservationArticleId(Long articleId);

	Long countByReservationArticleIdAndMemberIdIsNull(Long articleId);

	@Modifying
	@Query("DELETE FROM Reservation a WHERE a.reservationArticle.id = :postId")
	void deleteAllByReservationArticleId(@Param("postId") Long postId);
}

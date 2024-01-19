package com.kernel360.kernelsquare.domain.reservation.repository;

import com.kernel360.kernelsquare.domain.reservation.dto.ReservationDto;
import com.kernel360.kernelsquare.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<ReservationDto> findAllByReservationArticleId(Long articleId);

    Long countByReservationArticleIdAndMemberIdIsNull(Long articleId);

    @Modifying
    @Query("DELETE FROM Reservation a WHERE a.reservationArticle.id = :postId")
    void deleteAllByReservationArticleIdInBatch(@Param("postId") Long postId);
}

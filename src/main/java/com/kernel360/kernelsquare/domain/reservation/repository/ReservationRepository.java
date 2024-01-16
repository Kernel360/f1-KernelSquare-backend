package com.kernel360.kernelsquare.domain.reservation.repository;

import com.kernel360.kernelsquare.domain.reservation.dto.ReservationDto;
import com.kernel360.kernelsquare.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<ReservationDto> findAllByReservationArticleId(Long articleId);

    Long countByIdAndMemberIdIsNull(Long id);
}

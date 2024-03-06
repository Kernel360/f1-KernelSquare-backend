package com.kernelsquare.memberapi.domain.reservation.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kernelsquare.core.common_response.error.code.ReservationErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberRepository;
import com.kernelsquare.domainmysql.domain.reservation.entity.Reservation;
import com.kernelsquare.domainmysql.domain.reservation.repository.ReservationRepository;
import com.kernelsquare.domainmysql.domain.reservation_article.entity.ReservationArticle;
import com.kernelsquare.domainmysql.domain.reservation_article.repository.ReservationArticleRepository;
import com.kernelsquare.memberapi.domain.reservation.dto.AddReservationMemberRequest;
import com.kernelsquare.memberapi.domain.reservation.dto.AddReservationMemberResponse;
import com.kernelsquare.memberapi.domain.reservation.dto.FindAllReservationResponse;
import com.kernelsquare.memberapi.domain.reservation.dto.FindReservationResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {
	private final ReservationRepository reservationRepository;
	private final ReservationArticleRepository reservationArticleRepository;
	private final MemberRepository memberRepository;

	@Transactional(readOnly = true)
	public FindAllReservationResponse findAllReservationByMemberId(Long memberId) {
		List<Reservation> reservationList = reservationRepository.findAllByMemberId(memberId);

		List<FindReservationResponse> reservationResponseList = reservationList.stream()
			.map(FindReservationResponse::from)
			.toList();

		return FindAllReservationResponse.from(reservationResponseList);
	}

	@Transactional
	public void deleteReservationMember(Long reservationId) {
		Reservation reservation = reservationRepository.findById(reservationId)
			.orElseThrow(() -> new BusinessException(ReservationErrorCode.RESERVATION_NOT_FOUND));

		reservation.deleteMember();
	}

	@Transactional
	public AddReservationMemberResponse AddReservationMember(AddReservationMemberRequest addReservationMemberRequest,
		Long memberId) {
		// 해당 예약 게시글이 존재하는 지 확인
		ReservationArticle reservationArticle = reservationArticleRepository.findById(
				addReservationMemberRequest.reservationArticleId())
			.orElseThrow(() -> new BusinessException(ReservationErrorCode.RESERVATION_ARTICLE_NOT_FOUND));

		// 해당 예약 게시글의 예약 가능 시간이 초과되지 않았는 지 확인
		if (!reservationArticle.getStartTime().minusDays(7L).isBefore(LocalDateTime.now())
			&& !reservationArticle.getStartTime().minusDays(1L).isAfter(LocalDateTime.now())) {
			throw new BusinessException(ReservationErrorCode.RESERVATION_AVAILABLE_TIME_PASSED);
		}

		// 예약하려는 시간이 현재 시간보다 이전인지 체크(이전이면 예외처리)
		if (addReservationMemberRequest.reservationStartTime().isBefore(LocalDateTime.now())) {
			throw new BusinessException(ReservationErrorCode.RESERVATION_AVAILABLE_TIME_PASSED);
		}

		List<Reservation> reservationList = reservationRepository.findAllByMemberId(
			memberId);

		// 멘티가 한 번에 할 수 있는 최대 예약의 수는 10개
		if (reservationList.size() >= 10) {
			throw new BusinessException(ReservationErrorCode.RESERVATION_LIMIT_EXCEED);
		}

		// 멘티가 하나의 예약 게시글에 할 수 있는 최대 예약은 1개
		if (reservationRepository.existsByReservationArticleIdAndMemberId(
			addReservationMemberRequest.reservationArticleId(), memberId)) {
			throw new BusinessException(ReservationErrorCode.RESERVATION_ALREADY_EXIST);
		}

		Reservation reservation = reservationRepository.findById(addReservationMemberRequest.reservationId())
			.orElseThrow(() -> new BusinessException(ReservationErrorCode.RESERVATION_NOT_FOUND));

		if (reservation.getMember() != null) {
			throw new BusinessException(ReservationErrorCode.RESERVATION_ALREADY_TAKEN);
		}

		// 예약 중복 확인 체크하기
		for (Reservation bookedReservation : reservationList) {
			if (bookedReservation.getStartTime().equals(addReservationMemberRequest.reservationStartTime())) {
				throw new BusinessException(ReservationErrorCode.DUPLICATE_RESERVATION_TIME);
			}
		}

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new BusinessException(ReservationErrorCode.MEMBER_NOT_FOUND));

		reservation.addMember(member);

		return AddReservationMemberResponse.of(reservation);
	}
}

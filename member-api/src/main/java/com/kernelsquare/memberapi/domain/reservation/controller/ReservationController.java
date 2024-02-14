package com.kernelsquare.memberapi.domain.reservation.controller;

import static com.kernelsquare.core.common_response.response.code.ReservationResponseCode.*;

import com.kernelsquare.memberapi.domain.auth.service.MemberDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;
import com.kernelsquare.memberapi.domain.auth.dto.MemberPrincipal;
import com.kernelsquare.memberapi.domain.reservation.dto.AddReservationMemberRequest;
import com.kernelsquare.memberapi.domain.reservation.dto.AddReservationMemberResponse;
import com.kernelsquare.memberapi.domain.reservation.dto.FindAllReservationResponse;
import com.kernelsquare.memberapi.domain.reservation.service.ReservationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReservationController {
	private final ReservationService reservationService;

	@GetMapping("/coffeechat/reservations")
	public ResponseEntity<ApiResponse<FindAllReservationResponse>> findAllReservationByMemberId(@AuthenticationPrincipal
	MemberPrincipal memberPrincipal) {
		FindAllReservationResponse findAllReservationResponse = reservationService.findAllReservationByMemberId(
			memberPrincipal
				.getId());

		return ResponseEntityFactory.toResponseEntity(RESERVATION_ALL_FOUND, findAllReservationResponse);
	}

	@DeleteMapping("/coffeechat/reservations/{reservationId}")
	public ResponseEntity<ApiResponse> deleteReservation(@PathVariable Long reservationId) {
		reservationService.deleteReservationMember(reservationId);

		return ResponseEntityFactory.toResponseEntity(RESERVATION_DELETED);
	}

	@PutMapping("/coffeechat/reservations/book")
	public ResponseEntity<ApiResponse<AddReservationMemberResponse>> addReservationMember(
		@AuthenticationPrincipal MemberPrincipal memberPrincipal, @RequestBody
	AddReservationMemberRequest addReservationMemberRequest) {
		AddReservationMemberResponse addReservationMemberResponse = reservationService.AddReservationMember(
			addReservationMemberRequest, memberPrincipal.getId());

		return ResponseEntityFactory.toResponseEntity(RESERVATION_SUCCESS, addReservationMemberResponse);
	}
}

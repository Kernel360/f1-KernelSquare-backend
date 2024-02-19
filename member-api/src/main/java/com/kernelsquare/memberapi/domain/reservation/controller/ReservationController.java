package com.kernelsquare.memberapi.domain.reservation.controller;

import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdapter;
import com.kernelsquare.memberapi.domain.reservation.dto.AddReservationMemberRequest;
import com.kernelsquare.memberapi.domain.reservation.dto.AddReservationMemberResponse;
import com.kernelsquare.memberapi.domain.reservation.dto.FindAllReservationResponse;
import com.kernelsquare.memberapi.domain.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.kernelsquare.core.common_response.response.code.ReservationResponseCode.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReservationController {
	private final ReservationService reservationService;

	@GetMapping("/coffeechat/reservations")
	public ResponseEntity<ApiResponse<FindAllReservationResponse>> findAllReservationByMemberId(
		@AuthenticationPrincipal
		MemberAdapter memberAdapter) {
		FindAllReservationResponse findAllReservationResponse = reservationService.findAllReservationByMemberId(
			memberAdapter.getMember().getId());

		return ResponseEntityFactory.toResponseEntity(RESERVATION_ALL_FOUND, findAllReservationResponse);
	}

	@DeleteMapping("/coffeechat/reservations/{reservationId}")
	public ResponseEntity<ApiResponse> deleteReservation(@PathVariable Long reservationId) {
		reservationService.deleteReservationMember(reservationId);

		return ResponseEntityFactory.toResponseEntity(RESERVATION_DELETED);
	}

	@PutMapping("/coffeechat/reservations/book")
	public ResponseEntity<ApiResponse<AddReservationMemberResponse>> addReservationMember(
		@AuthenticationPrincipal MemberAdapter memberAdapter, @RequestBody
	AddReservationMemberRequest addReservationMemberRequest) {
		AddReservationMemberResponse addReservationMemberResponse = reservationService.AddReservationMember(
			addReservationMemberRequest, memberAdapter.getMember().getId());

		return ResponseEntityFactory.toResponseEntity(RESERVATION_SUCCESS, addReservationMemberResponse);
	}
}

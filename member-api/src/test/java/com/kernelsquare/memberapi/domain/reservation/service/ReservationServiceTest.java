package com.kernelsquare.memberapi.domain.reservation.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kernelsquare.domainmysql.domain.coffeechat.entity.ChatRoom;
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

@DisplayName("예약 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {
	@InjectMocks
	private ReservationService reservationService;
	@Mock
	private ReservationRepository reservationRepository;
	@Mock
	private ReservationArticleRepository reservationArticleRepository;
	@Mock
	private MemberRepository memberRepository;

	@Test
	@DisplayName("회원 ID로 모든 예약 조회 테스트")
	void testFindAllReservationByMemberId() {
		//given
		Long memberId = 1L;

		ChatRoom chatRoom = ChatRoom.builder()
			.id(1L)
			.roomKey("randomRoom")
			.build();

		Reservation reservation = Reservation.builder()
			.reservationArticle(null)
			.chatRoom(chatRoom)
			.startTime(LocalDateTime.now())
			.endTime(LocalDateTime.now())
			.build();

		List<Reservation> reservationList = List.of(reservation);

		doReturn(reservationList)
			.when(reservationRepository)
			.findAllByMemberId(anyLong());

		//when
		FindAllReservationResponse findAllReservationResponse = reservationService.findAllReservationByMemberId(
			memberId);
		FindReservationResponse reservationResponse = findAllReservationResponse.reservationResponses().get(0);

		//then
		assertThat(findAllReservationResponse).isNotNull();
		assertThat(reservationResponse.startTime()).isEqualTo(reservation.getStartTime());

		//verify
		verify(reservationRepository, atMostOnce()).findAllByMemberId(anyLong());
		verify(reservationRepository, only()).findAllByMemberId(anyLong());
	}

	@Test
	@DisplayName("멘티가 예약하면 예약 객체에 멘티가 추가된다.")
	void testAddReservationMember() throws Exception {
		//given
		Member member = Member.builder()
			.id(4L)
			.nickname("김주과아")
			.experience(121200L)
			.imageUrl("s3:D12d12d")
			.email("klerber@nav.erco")
			.authorities(List.of())
			.introduction("난집에가고싶은")
			.password("hashed_password")
			.build();

		Optional<Member> memberOptional = Optional.of(member);

		ReservationArticle reservationArticle = ReservationArticle.builder()
			.id(3L)
			.title("최고로멋진나는김원상")
			.content("나는스프링의아이돌")
			.hashtagList(List.of())
			.member(member)
			.build();

		reservationArticle.addStartTime(LocalDateTime.now());

		Optional<ReservationArticle> reservationArticleOptional = Optional.of(reservationArticle);

		List<Reservation> reservationList = new ArrayList<>();

		Reservation newReservation = Reservation.builder()
			.reservationArticle(reservationArticle)
			.startTime(LocalDateTime.now().plusDays(1L))
			.endTime(LocalDateTime.now().plusDays(1L).plusMinutes(30))
			.build();

		Optional<Reservation> reservationOptional = Optional.of(newReservation);

		AddReservationMemberRequest addReservationMemberRequest = AddReservationMemberRequest
			.builder()
			.reservationId(1L)
			.reservationArticleId(3L)
			.reservationStartTime(LocalDateTime.now().plusSeconds(1L))
			.build();

		reservationList.add(newReservation);

		doReturn(reservationArticleOptional)
			.when(reservationArticleRepository)
			.findById(anyLong());

		doReturn(reservationList)
			.when(reservationRepository)
			.findAllByMemberId(anyLong());

		doReturn(Boolean.FALSE)
			.when(reservationRepository)
			.existsByReservationArticleIdAndMemberId(anyLong(), anyLong());

		doReturn(reservationOptional)
			.when(reservationRepository)
			.findById(anyLong());

		doReturn(memberOptional)
			.when(memberRepository)
			.findById(anyLong());

		//when
		AddReservationMemberResponse addReservationMemberResponse = reservationService.AddReservationMember(
			addReservationMemberRequest, member.getId());

		//then
		assertThat(addReservationMemberResponse).isNotNull();
		assertThat(addReservationMemberResponse.reservationArticleId()).isEqualTo(reservationArticle.getId());

		//verify
		verify(reservationArticleRepository, times(1)).findById(anyLong());
		verify(reservationArticleRepository, only()).findById(anyLong());
		verify(reservationRepository, times(1)).findAllByMemberId(anyLong());
		verify(reservationRepository, times(1)).findById(anyLong());
		verify(reservationRepository, times(1)).existsByReservationArticleIdAndMemberId(anyLong(), anyLong());
		verify(memberRepository, times(1)).findById(anyLong());
		verify(memberRepository, only()).findById(anyLong());
	}
}
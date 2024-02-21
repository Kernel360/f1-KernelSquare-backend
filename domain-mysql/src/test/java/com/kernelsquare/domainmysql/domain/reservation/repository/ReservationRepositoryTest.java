package com.kernelsquare.domainmysql.domain.reservation.repository;

import com.kernelsquare.domainmysql.config.DBConfig;
import com.kernelsquare.domainmysql.domain.coffeechat.entity.ChatRoom;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.reservation.entity.Reservation;
import com.kernelsquare.domainmysql.domain.reservation_article.entity.ReservationArticle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("예약 레포지토리 단위 테스트")
@DataJpaTest
@Import(DBConfig.class)
class ReservationRepositoryTest {
    @Autowired
    private ReservationRepository reservationRepository;

    private Member testMember;
    private ChatRoom testChatRoom;
    private Reservation testReservation;
    private ReservationArticle testReservationArticle;

    private Long testMemberId = 1L;
    private Long testReservationArticleId = 1L;
    private LocalDateTime testStartTime = LocalDateTime.now();
    private LocalDateTime testExpireTime = LocalDateTime.now().plusMinutes(30L);

    @BeforeEach
    void setUp() {
        testMember = createMember(testMemberId, "test@email.com");
        testChatRoom = createChatRoom("testRoomKey", testExpireTime);
        testReservationArticle = createReservationArticle(testReservationArticleId, testMember);
        testReservation = createTestReservation(testStartTime, testExpireTime, testReservationArticle, testChatRoom);
        testReservation.addMember(testMember);
    }

    @Test
    @DisplayName("회원 아이디와 예약창 아이디에 따른 예약 존재 여부 조회")
    void testExistsByReservationArticleIdAndMemberId() {
        //given
        reservationRepository.save(testReservation);

        //when
        Boolean reservationExistence = reservationRepository.existsByReservationArticleIdAndMemberId(
                testReservationArticleId, testMemberId);

        //then
        assertTrue(reservationExistence);
    }

    @Test
    @DisplayName("예약창이 이미 존재하는지 조회 (현재 시점에서 뒤에 남아있는 예약 존재 여부 확인)")
    void testExistsByMemberIdAndFinishedIsFalseAndEndTimeAfter() {
        //given
        reservationRepository.save(testReservation);

        //when
        Boolean reservationExpected = reservationRepository.existsByMemberIdAndEndTimeAfter(
                testMemberId, LocalDateTime.now());

        //then
        assertTrue(reservationExpected);
    }

    @Test
    @DisplayName("예약창 아이디로 전체 예약 조회")
    void testFindAllByReservationArticleId() {
        //given
        reservationRepository.save(testReservation);

        //when
        List<Reservation> reservationList = reservationRepository.findAllByReservationArticleId(testReservationArticleId);

        //then
        assertThat(reservationList).hasSize(1);
        assertThat(reservationList.get(0).getStartTime()).isEqualTo(testStartTime);
    }

    @Test
    @DisplayName("회원 아이디로 전체 예약 조회")
    void testFindAllByMemberId() {
        //given
        reservationRepository.save(testReservation);

        //when
        List<Reservation> reservationList = reservationRepository.findAllByMemberId(testMemberId);

        //then
        assertThat(reservationList).hasSize(1);
        assertThat(reservationList.get(0).getStartTime()).isEqualTo(testStartTime);
    }

    @Test
    @DisplayName("예약창 아이디로 전체 예약 개수 조회")
    void testCountAllByReservationArticleId() {
        //given
        reservationRepository.save(testReservation);

        //when
        Long reservationCount = reservationRepository.countAllByReservationArticleId(testReservationArticleId);

        //then
        assertThat(reservationCount).isEqualTo(1);
    }

    @Test
    @DisplayName("예약창 아이디가 있으며 회원 아이디가 존재하지 않는 예약 개수 조회")
    void testCountByReservationArticleIdAndMemberIdIsNull() {
        //given
        testReservation.deleteMember();
        reservationRepository.save(testReservation);

        //when
        Long reservationCount = reservationRepository.countAllByReservationArticleId(testReservationArticleId);

        //then
        assertThat(reservationCount).isEqualTo(1);
    }

    @Test
    @DisplayName("예약창에 존재하는 모든 예약 삭제")
    void testDeleteAllByReservationArticleId() {
        //given
        reservationRepository.save(testReservation);

        //when
        reservationRepository.deleteAllByReservationArticleId(testReservationArticleId);
        Long reservationCount = reservationRepository.countAllByReservationArticleId(testReservationArticleId);

        //then
        assertThat(reservationCount).isEqualTo(0);
    }

    private Member createMember(
            Long id,
            String email
    ) {
        return Member.builder()
                .id(id)
                .nickname("테스트 유저")
                .email(email)
                .password("Password1234!")
                .experience(0L)
                .imageUrl("imageUrl")
                .introduction("테스트 소개")
                .build();
    }

    private ChatRoom createChatRoom(
            String roomKey,
            LocalDateTime expirationTime
    ) {
        return ChatRoom.builder()
                .roomKey(roomKey)
                .expirationTime(expirationTime)
                .build();
    }

    private Reservation createTestReservation(
            LocalDateTime startTime,
            LocalDateTime endTime,
            ReservationArticle reservationArticle,
            ChatRoom chatRoom
    ) {
        return Reservation.builder()
                .startTime(startTime)
                .endTime(endTime)
                .reservationArticle(reservationArticle)
                .chatRoom(chatRoom)
                .build();
    }

    private ReservationArticle createReservationArticle(
            Long id,
            Member member
    ) {
        return ReservationArticle.builder()
                .id(id)
                .member(member)
                .title("테스트 예약창")
                .content("테스트 예약창 내용")
                .build();
    }
}

package com.kernel360.kernelsquare.domain.reservation_article.service;

import com.kernel360.kernelsquare.domain.authority.entity.Authority;
import com.kernel360.kernelsquare.domain.coffeechat.entity.ChatRoom;
import com.kernel360.kernelsquare.domain.coffeechat.repository.CoffeeChatRepository;
import com.kernel360.kernelsquare.domain.hashtag.entity.Hashtag;
import com.kernel360.kernelsquare.domain.hashtag.repository.HashtagRepository;
import com.kernel360.kernelsquare.domain.level.entity.Level;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.member.repository.MemberRepository;
import com.kernel360.kernelsquare.domain.member_authority.entity.MemberAuthority;
import com.kernel360.kernelsquare.domain.reservation.entity.Reservation;
import com.kernel360.kernelsquare.domain.reservation.repository.ReservationRepository;
import com.kernel360.kernelsquare.domain.reservation_article.dto.CreateReservationArticleRequest;
import com.kernel360.kernelsquare.domain.reservation_article.dto.CreateReservationArticleResponse;
import com.kernel360.kernelsquare.domain.reservation_article.dto.FindAllReservationArticleResponse;
import com.kernel360.kernelsquare.domain.reservation_article.dto.FindReservationArticleResponse;
import com.kernel360.kernelsquare.domain.reservation_article.entity.ReservationArticle;
import com.kernel360.kernelsquare.domain.reservation_article.repository.ReservationArticleRepository;
import com.kernel360.kernelsquare.global.domain.AuthorityType;
import com.kernel360.kernelsquare.global.dto.PageResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("커피챗 예약게시글 서비스 단위 테스트")
@ExtendWith(MockitoExtension.class)
class ReservationArticleServiceTest {
    @InjectMocks
    private ReservationArticleService reservationArticleService;
    @Mock
    private ReservationArticleRepository reservationArticleRepository;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private CoffeeChatRepository coffeeChatRepository;
    @Mock
    private HashtagRepository hashTagRepository;
    @Mock
    private MemberRepository memberRepository;

    private Member member;
    private Authority authority;
    private MemberAuthority memberRole;
    private Level level;
    private Hashtag hashtag;
    private ChatRoom chatRoom;

    private ReservationArticle createTestReservationArticle(Long id) {
        return ReservationArticle.builder()
                .id(id)
                .member(member)
                .title("testplz")
                .content("ahahahahahhhh")
                .build();
    }

    private Hashtag createTestHashtag() {
        return Hashtag.builder()
                .content("#tester333")
                .build();
    }

    private ChatRoom createChatRoom() {
        return ChatRoom
                .builder()
                .id(1L)
                .roomKey("testRoomKey")
                .build();
    }

    private Reservation createTestReservation(ChatRoom chatRoom) {
        return Reservation.builder()
                .chatRoom(chatRoom)
                .startTime(LocalDateTime.now())
                .build();
    }

    private Member createTestMember() {
        return Member.builder()
                .id(1L)
                .nickname("hongjugwang")
                .email("jugwang@naver.com")
                .password("hashedPassword")
                .experience(10000L)
                .introduction("hi, i'm hongjugwang.")
                .imageUrl("s3:qwe12fasdawczx")
                .level(level)
                .build();
    }

    private Level createTestLevel() {
        return Level.builder()
                .name(6L)
                .imageUrl("1.jpg")
                .build();
    }

    private MemberAuthority createTestRole(Member member, Authority authority) {
        return MemberAuthority.builder()
                .member(member)
                .authority(authority)
                .build();
    }

    private Authority createTestAuthority() {
        return Authority.builder()
                .id(1L)
                .authorityType(AuthorityType.ROLE_MENTOR)
                .build();
    }
    @Test
    @DisplayName("예약게시글 생성 테스트")
    void testCreateReservationArticle() {
        // Given
        member = createTestMember();
        authority = createTestAuthority();
        memberRole = createTestRole(member, authority);
        hashtag = createTestHashtag();

        List<MemberAuthority> memberAuthorityList = List.of(memberRole);

        member.initAuthorities(memberAuthorityList);

        ReservationArticle reservationArticle = createTestReservationArticle(1L);

        CreateReservationArticleRequest createReservationArticleRequest =
                new CreateReservationArticleRequest(member.getId(), reservationArticle.getTitle(), reservationArticle.getContent(),
                        reservationArticle.getHashtagList().stream().map(Hashtag::getContent).toList(), List.of(LocalDateTime.now(),LocalDateTime.now().plusDays(2)));

        given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(member));
        given(reservationArticleRepository.save(any(ReservationArticle.class))).willReturn(reservationArticle);

        // When
        CreateReservationArticleResponse createReservationArticleResponse = reservationArticleService.createReservationArticle(createReservationArticleRequest);

        // Then
        assertThat(createReservationArticleResponse).isNotNull();
        assertThat(createReservationArticleResponse.reservationArticleId()).isEqualTo(reservationArticle.getId());

        // Verify
        verify(reservationArticleRepository, times(1)).save(any(ReservationArticle.class));
    }

    @Test
    @DisplayName("모든 예약창 조회 테스트")
    void testFindAllReservationArticle() {
        // Given
        level = createTestLevel();
        member = createTestMember();
        ReservationArticle reservationArticle1 = createTestReservationArticle(1L);
        ReservationArticle reservationArticle2 = createTestReservationArticle(2L);
        List<ReservationArticle> articles = List.of(reservationArticle1, reservationArticle2);

        Pageable pageable = PageRequest.of(0,2);
        Page<ReservationArticle> pages = new PageImpl<>(articles, pageable, articles.size());

        given(reservationArticleRepository.findAll(any(PageRequest.class))).willReturn(pages);

        Integer currentPage = pageable.getPageNumber() + 1;

        Integer totalPages = pages.getTotalPages();

        if (totalPages == 0) totalPages+=1;

        // When
        PageResponse<FindAllReservationArticleResponse> pageResponse = reservationArticleService.findAllReservationArticle(pageable);

        // Then
        assertThat(pageResponse).isNotNull();
        assertThat(pageResponse.pagination().totalPage()).isEqualTo(totalPages);
        assertThat(pageResponse.pagination().pageable()).isEqualTo(pages.getSize());
        assertThat(pageResponse.pagination().isEnd()).isEqualTo(currentPage.equals(totalPages));
        assertThat(pageResponse.list()).isNotNull();

        // Verify
        verify(reservationArticleRepository, times(1)).findAll(any(PageRequest.class));
    }


    @Test
    @DisplayName("예약창 조회 테스트")
    void testFindReservationArticle() {
        // Given
        level = createTestLevel();
        member = createTestMember();
        hashtag = createTestHashtag();
        chatRoom = createChatRoom();
        // reservation = createTestReservation(chatRoom);

        ReservationArticle reservationArticle = createTestReservationArticle(1L);
        Hashtag testHashtag = createTestHashtag();
//        List<String> expectedHashtags = List.of(testHashtag).stream()
//                .map(Hashtag::getContent).toList();
        Reservation testReservation = createTestReservation(chatRoom);
//        List<LocalDateTime> expectedDateTimes = List.of(testReservation).stream()
//                .map(Reservation::getStartTime).toList();

        given(reservationArticleRepository.findById(anyLong())).willReturn(Optional.ofNullable(reservationArticle));
        // given(reservation.getChatRoom().getId()).willReturn(anyLong());
        given(hashTagRepository.findAllByReservationArticleId(anyLong())).willReturn(List.of(testHashtag));
        given(reservationRepository.findAllByReservationArticleId(anyLong())).willReturn(List.of(testReservation));

        // When
        FindReservationArticleResponse findReservationArticleResponse = reservationArticleService.findReservationArticle(reservationArticle.getId());

        // Then
        assertThat(findReservationArticleResponse).isNotNull();
        assertThat(findReservationArticleResponse.articleId()).isEqualTo(reservationArticle.getId());
        assertThat(findReservationArticleResponse.title()).isEqualTo(reservationArticle.getTitle());
        assertThat(findReservationArticleResponse.content()).isEqualTo(reservationArticle.getContent());
        assertThat(findReservationArticleResponse.level()).isEqualTo(reservationArticle.getMember().getLevel().getName());
        assertThat(findReservationArticleResponse.levelImageUrl().length()).isGreaterThan(reservationArticle.getMember().getLevel().getImageUrl().length());
        assertThat(findReservationArticleResponse.levelImageUrl()).endsWith(reservationArticle.getMember().getLevel().getImageUrl());
        assertThat(findReservationArticleResponse.nickname()).isEqualTo(reservationArticle.getMember().getNickname());
        assertThat(findReservationArticleResponse.memberId()).isEqualTo(reservationArticle.getMember().getId());
        assertThat(findReservationArticleResponse.memberImageUrl().length()).isGreaterThan(reservationArticle.getMember().getImageUrl().length());
        assertThat(findReservationArticleResponse.memberImageUrl()).endsWith(reservationArticle.getMember().getImageUrl());
        assertThat(findReservationArticleResponse.hashtags().get(0).content()).isEqualTo(testHashtag.getContent());
        assertThat(findReservationArticleResponse.dateTimes().get(0).reservationId()).isEqualTo(testReservation.getId());
        // TODO 출처가 어디인지 response 에서 확인 필요, 모두 다 reservationArticle 에서 가져와서 비교하는게 아닌 Service 가 하는 일을 보고 맞게 스터빙해서 비교해야함. hashtag, reservation

        // Verify
        verify(reservationArticleRepository, times(1)).findById(anyLong());
        verify(hashTagRepository, times(1)).findAllByReservationArticleId(anyLong());
        verify(reservationRepository, times(1)).findAllByReservationArticleId(anyLong());
    }

    @Test
    @DisplayName("예약창 삭제 테스트")
    void testDeleteReservationArticle() {
        // Given
        level = createTestLevel();
        member = createTestMember();
        ReservationArticle reservationArticle = createTestReservationArticle(1L);

        given(reservationArticleRepository.findById(anyLong())).willReturn(Optional.ofNullable(reservationArticle));

        doNothing()
                .when(reservationArticleRepository)
                .deleteById(anyLong());

        doNothing()
                .when(coffeeChatRepository)
                .deleteChatRoom(anyLong());

        doNothing()
                .when(reservationRepository)
                .deleteAllByReservationArticleId(anyLong());

        doNothing()
                .when(hashTagRepository)
                .deleteAllByReservationArticleId(anyLong());

        // When
        reservationArticleService.deleteReservationArticle(reservationArticle.getId());

        // Then
        verify(reservationArticleRepository, times(1)).deleteById(anyLong());
    }

}
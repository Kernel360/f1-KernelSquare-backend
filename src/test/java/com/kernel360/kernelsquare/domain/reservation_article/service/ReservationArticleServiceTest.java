package com.kernel360.kernelsquare.domain.reservation_article.service;

import com.kernel360.kernelsquare.domain.authority.entity.Authority;
import com.kernel360.kernelsquare.domain.authority.repository.AuthorityRepository;
import com.kernel360.kernelsquare.domain.hashtag.entity.HashTag;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.member.repository.MemberRepository;
import com.kernel360.kernelsquare.domain.member_authority.entity.MemberAuthority;
import com.kernel360.kernelsquare.domain.member_authority.repository.MemberAuthorityRepository;
import com.kernel360.kernelsquare.domain.reservation_article.dto.CreateReservationArticleRequest;
import com.kernel360.kernelsquare.domain.reservation_article.dto.CreateReservationArticleResponse;
import com.kernel360.kernelsquare.domain.reservation_article.dto.FindAllReservationArticleResponse;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("커피챗 예약게시글 서비스 단위 테스트")
@ExtendWith(MockitoExtension.class)
class ReservationArticleServiceTest {
    @InjectMocks
    private ReservationArticleService reservationArticleService;
    @Mock
    private ReservationArticleRepository reservationArticleRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private MemberAuthorityRepository memberAuthorityRepository;
    @Mock
    private AuthorityRepository authorityRepository;

    private Member member;
    private Authority authority;
    private MemberAuthority memberRole;

    private ReservationArticle createTestReservationArticle(Long id) {
        return ReservationArticle.builder()
                .id(id)
                .title("testplz")
                .content("ahahahahahhhh")
                .hashTagList(List.of())
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
                .build();
    }

    private MemberAuthority createTestRole(Member member, Authority authority) {
        return MemberAuthority.builder()
                .member(member)
                .authority(authority)
                .build();
    }

    @Test
    @DisplayName("예약게시글 생성 테스트")
    void testCreateReservationArticle() {
        // Given
        member = createTestMember();
        authority = createTestAuthority();
        memberRole = createTestRole(member, authority);

        List<MemberAuthority> memberAuthorityList = List.of(memberRole);

        member.initAuthorities(memberAuthorityList);

        ReservationArticle reservationArticle = createTestReservationArticle(1L);

        CreateReservationArticleRequest createReservationArticleRequest =
                new CreateReservationArticleRequest(member.getId(), reservationArticle.getTitle(), reservationArticle.getContent(),
                        reservationArticle.getHashTagList().stream().map(HashTag::getContent).toList(), List.of(LocalDateTime.now(),LocalDateTime.now().plusDays(2)));

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

    private Authority createTestAuthority() {
        return Authority.builder()
                .id(1L)
                .authorityType(AuthorityType.ROLE_MENTOR)
                .build();
    }

    @Test
    @DisplayName("모든 예약창 조회 테스트")
    void testFindAllReservationArticle() {
        // Given
        member = createTestMember();
        ReservationArticle reservationArticle1 = createTestReservationArticle(1L);
        ReservationArticle reservationArticle2 = createTestReservationArticle(2L);
        List<ReservationArticle> articles = List.of(reservationArticle1, reservationArticle2);

        Pageable pageable = PageRequest.of(0,2);
        Page<ReservationArticle> pages = new PageImpl<>(articles, pageable, articles.size());

        given(reservationArticleRepository.findById(reservationArticle1.getId())).willReturn(Optional.of(reservationArticle1));
        given(reservationArticleRepository.findById(reservationArticle2.getId())).willReturn(Optional.of(reservationArticle2));
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

}
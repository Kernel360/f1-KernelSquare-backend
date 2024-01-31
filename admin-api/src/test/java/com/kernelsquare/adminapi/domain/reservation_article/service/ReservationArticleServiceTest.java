package com.kernelsquare.adminapi.domain.reservation_article.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

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

import com.kernelsquare.adminapi.domain.reservation_article.dto.FindAllReservationArticleResponse;
import com.kernelsquare.adminapi.domain.reservation_article.dto.FindReservationArticleResponse;
import com.kernelsquare.core.dto.PageResponse;
import com.kernelsquare.core.type.AuthorityType;
import com.kernelsquare.domainmysql.domain.authority.entity.Authority;
import com.kernelsquare.domainmysql.domain.coffeechat.repository.CoffeeChatRepository;
import com.kernelsquare.domainmysql.domain.hashtag.entity.Hashtag;
import com.kernelsquare.domainmysql.domain.hashtag.repository.HashtagRepository;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberRepository;
import com.kernelsquare.domainmysql.domain.member_authority.entity.MemberAuthority;
import com.kernelsquare.domainmysql.domain.reservation.repository.ReservationRepository;
import com.kernelsquare.domainmysql.domain.reservation_article.entity.ReservationArticle;
import com.kernelsquare.domainmysql.domain.reservation_article.repository.ReservationArticleRepository;

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

	private ReservationArticle createTestReservationArticle(Long id) {
		return ReservationArticle.builder()
			.id(id)
			.member(member)
			.title("testplz")
			.content("ahahahahahhhh")
			.hashtagList(List.of())
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
	@DisplayName("모든 예약창 조회 테스트")
	void testFindAllReservationArticle() {
		// Given
		level = createTestLevel();
		member = createTestMember();
		ReservationArticle reservationArticle1 = createTestReservationArticle(1L);
		ReservationArticle reservationArticle2 = createTestReservationArticle(2L);
		List<ReservationArticle> articles = List.of(reservationArticle1, reservationArticle2);

		Pageable pageable = PageRequest.of(0, 2);
		Page<ReservationArticle> pages = new PageImpl<>(articles, pageable, articles.size());

		given(reservationArticleRepository.findAll(any(PageRequest.class))).willReturn(pages);

		Integer currentPage = pageable.getPageNumber() + 1;

		Integer totalPages = pages.getTotalPages();

		if (totalPages == 0)
			totalPages += 1;

		// When
		PageResponse<FindAllReservationArticleResponse> pageResponse = reservationArticleService.findAllReservationArticle(
			pageable);

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
		ReservationArticle reservationArticle = createTestReservationArticle(1L);

		given(reservationArticleRepository.findById(anyLong())).willReturn(Optional.ofNullable(reservationArticle));

		// When
		FindReservationArticleResponse findReservationArticleResponse = reservationArticleService.findReservationArticle(
			reservationArticle.getId());

		// Then
		assertThat(findReservationArticleResponse).isNotNull();
		assertThat(findReservationArticleResponse.articleId()).isEqualTo(reservationArticle.getId());
		assertThat(findReservationArticleResponse.title()).isEqualTo(reservationArticle.getTitle());
		assertThat(findReservationArticleResponse.content()).isEqualTo(reservationArticle.getContent());
		assertThat(findReservationArticleResponse.level()).isEqualTo(
			reservationArticle.getMember().getLevel().getName());
		assertThat(findReservationArticleResponse.levelImageUrl().length()).isGreaterThan(
			reservationArticle.getMember().getLevel().getImageUrl().length());
		assertThat(findReservationArticleResponse.levelImageUrl()).endsWith(
			reservationArticle.getMember().getLevel().getImageUrl());
		assertThat(findReservationArticleResponse.nickname()).isEqualTo(reservationArticle.getMember().getNickname());
		assertThat(findReservationArticleResponse.memberId()).isEqualTo(reservationArticle.getMember().getId());
		assertThat(findReservationArticleResponse.memberImageUrl().length()).isGreaterThan(
			reservationArticle.getMember().getImageUrl().length());
		assertThat(findReservationArticleResponse.memberImageUrl()).endsWith(
			reservationArticle.getMember().getImageUrl());
		assertThat(findReservationArticleResponse.hashTagList()).isEqualTo(reservationArticle.getHashtagList()
			.stream().map(Hashtag::getContent).toList());

		// Verify
		verify(reservationArticleRepository, times(1)).findById(anyLong());
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
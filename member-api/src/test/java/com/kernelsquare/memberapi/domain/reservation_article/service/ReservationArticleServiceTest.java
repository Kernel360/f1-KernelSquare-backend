package com.kernelsquare.memberapi.domain.reservation_article.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
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

import com.kernelsquare.core.common_response.error.code.ReservationArticleErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.core.dto.PageResponse;
import com.kernelsquare.core.type.AuthorityType;
import com.kernelsquare.domainmysql.domain.authority.entity.Authority;
import com.kernelsquare.domainmysql.domain.coffeechat.entity.ChatRoom;
import com.kernelsquare.domainmysql.domain.coffeechat.repository.CoffeeChatRepository;
import com.kernelsquare.domainmysql.domain.hashtag.entity.Hashtag;
import com.kernelsquare.domainmysql.domain.hashtag.repository.HashtagRepository;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberRepository;
import com.kernelsquare.domainmysql.domain.member_authority.entity.MemberAuthority;
import com.kernelsquare.domainmysql.domain.reservation.entity.Reservation;
import com.kernelsquare.domainmysql.domain.reservation.repository.ReservationRepository;
import com.kernelsquare.domainmysql.domain.reservation_article.entity.ReservationArticle;
import com.kernelsquare.domainmysql.domain.reservation_article.repository.ReservationArticleRepository;
import com.kernelsquare.memberapi.domain.reservation_article.dto.CreateReservationArticleRequest;
import com.kernelsquare.memberapi.domain.reservation_article.dto.CreateReservationArticleResponse;
import com.kernelsquare.memberapi.domain.reservation_article.dto.FindAllReservationArticleResponse;
import com.kernelsquare.memberapi.domain.reservation_article.dto.FindReservationArticleResponse;

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

	private Reservation createTestReservationStartTime(ChatRoom chatRoom, LocalDateTime startTime) {
		return Reservation.builder()
			.chatRoom(chatRoom)
			.startTime(startTime)
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

		List<MemberAuthority> memberAuthorityList = List.of(memberRole);

		member.initAuthorities(memberAuthorityList);

		ReservationArticle reservationArticle = createTestReservationArticle(1L);

		LocalDateTime localDateTime = LocalDateTime.now().plusDays(8L);

		reservationArticle.addStartTime(localDateTime);

		Hashtag hashtag = createTestHashtag();

		CreateReservationArticleRequest createReservationArticleRequest =
			new CreateReservationArticleRequest(member.getId(), reservationArticle.getTitle(),
				reservationArticle.getContent(),
				reservationArticle.getIntroduction(),
				List.of(hashtag.getContent()),
				List.of(LocalDateTime.now().plusDays(7), LocalDateTime.now().plusDays(8)));

		given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(member));
		given(reservationRepository.existsByMemberIdAndEndTimeAfter(eq(member.getId()),
			any(LocalDateTime.class))).willReturn(
			false
		);
		given(reservationArticleRepository.save(any(ReservationArticle.class))).willReturn(reservationArticle);

		// When
		CreateReservationArticleResponse createReservationArticleResponse = reservationArticleService.createReservationArticle(
			createReservationArticleRequest);

		// Then
		assertThat(createReservationArticleResponse).isNotNull();
		assertThat(createReservationArticleResponse.reservationArticleId()).isEqualTo(reservationArticle.getId());

		// Verify
		verify(memberRepository, times(1)).findById(anyLong());
		verify(reservationRepository, times(1)).existsByMemberIdAndEndTimeAfter(anyLong(), any(LocalDateTime.class));
		verify(reservationArticleRepository, times(1)).save(any(ReservationArticle.class));
	}

	@Test
	@DisplayName("예약창 예약 생성 가능 일시(LocalDateTime) 7일 이후 1달 이내 초과시 에러 메시지")
	void testCreateReservationArticleExceedDateTime() {
		// Given
		member = createTestMember();
		authority = createTestAuthority();
		memberRole = createTestRole(member, authority);

		List<MemberAuthority> memberAuthorityList = List.of(memberRole);

		member.initAuthorities(memberAuthorityList);

		ReservationArticle reservationArticle = createTestReservationArticle(1L);

		LocalDateTime localDateTime = LocalDateTime.now().plusDays(8L);

		reservationArticle.addStartTime(localDateTime);

		Hashtag hashtag = createTestHashtag();

		CreateReservationArticleRequest createReservationArticleRequest =
			new CreateReservationArticleRequest(member.getId(), reservationArticle.getTitle(),
				reservationArticle.getContent(),
				reservationArticle.getIntroduction(),
				List.of(hashtag.getContent()),
				List.of(LocalDateTime.now().plusDays(40L), LocalDateTime.now().plusDays(41L)));

		given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(member));
		given(reservationRepository.existsByMemberIdAndEndTimeAfter(anyLong(), any(LocalDateTime.class))).willReturn(
			false
		);
		given(reservationArticleRepository.save(any(ReservationArticle.class))).willReturn(reservationArticle);

		// When

		// Then
		assertThatThrownBy(() ->
			reservationArticleService.createReservationArticle(createReservationArticleRequest))
			.isExactlyInstanceOf(BusinessException.class)
			.extracting("errorCode")
			.isEqualTo(ReservationArticleErrorCode.RESERVATION_PERIOD_LIMIT);

		// Verify
		verify(memberRepository, times(1)).findById(anyLong());
		verify(reservationRepository, times(1)).existsByMemberIdAndEndTimeAfter(anyLong(), any(LocalDateTime.class));
		verify(reservationArticleRepository, times(1)).save(any(ReservationArticle.class));
	}

	@Test
	@DisplayName("예약창 예약 생성 가능 일시(LocalDateTime) 구간 3일 초과시 에러 메시지")
	void testCreateReservationArticleExceedDateTimeInterval() {
		// Given
		member = createTestMember();
		authority = createTestAuthority();
		memberRole = createTestRole(member, authority);

		List<MemberAuthority> memberAuthorityList = List.of(memberRole);

		member.initAuthorities(memberAuthorityList);

		ReservationArticle reservationArticle = createTestReservationArticle(1L);

		LocalDateTime localDateTime = LocalDateTime.now().plusDays(8L);

		reservationArticle.addStartTime(localDateTime);

		Hashtag hashtag = createTestHashtag();

		CreateReservationArticleRequest createReservationArticleRequest =
			new CreateReservationArticleRequest(member.getId(), reservationArticle.getTitle(),
				reservationArticle.getContent(),
				reservationArticle.getIntroduction(),
				List.of(hashtag.getContent()),
				List.of(LocalDateTime.now().plusDays(7L), LocalDateTime.now().plusDays(11L).plusMinutes(30L)));

		given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(member));
		given(reservationRepository.existsByMemberIdAndEndTimeAfter(anyLong(), any(LocalDateTime.class))).willReturn(
			false
		);
		given(reservationArticleRepository.save(any(ReservationArticle.class))).willReturn(reservationArticle);

		// When

		// Then
		assertThatThrownBy(() ->
			reservationArticleService.createReservationArticle(createReservationArticleRequest))
			.isExactlyInstanceOf(BusinessException.class)
			.extracting("errorCode")
			.isEqualTo(ReservationArticleErrorCode.RESERVATION_TIME_LIMIT);

		// Verify
		verify(memberRepository, times(1)).findById(anyLong());
		verify(reservationRepository, times(1)).existsByMemberIdAndEndTimeAfter(anyLong(), any(LocalDateTime.class));
		verify(reservationArticleRepository, times(1)).save(any(ReservationArticle.class));
	}

	@Test
	@DisplayName("모든 예약창 조회 테스트")
	void testFindAllReservationArticle() {
		// Given
		level = createTestLevel();
		member = createTestMember();

		Hashtag hashtag1 = createTestHashtag();
		Hashtag hashtag2 = createTestHashtag();

		List<Hashtag> hashtagList = List.of(hashtag1, hashtag2);

		ReservationArticle reservationArticle1 = ReservationArticle.builder()
			.id(1L)
			.member(member)
			.title("testplz")
			.content("ahahahahahhhh")
			.hashtagList(hashtagList)
			.build();

		reservationArticle1.addStartTime(LocalDateTime.now());

		ReservationArticle reservationArticle2 = ReservationArticle.builder()
			.id(2L)
			.member(member)
			.title("testplz22")
			.content("ahahahahahhhh2222")
			.hashtagList(hashtagList)
			.build();

		reservationArticle2.addStartTime(LocalDateTime.now());

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
		hashtag = createTestHashtag();
		chatRoom = createChatRoom();

		ReservationArticle reservationArticle = createTestReservationArticle(1L);
		Hashtag testHashtag = createTestHashtag();
		Reservation testReservation = createTestReservation(chatRoom);

		given(reservationArticleRepository.findById(anyLong())).willReturn(Optional.ofNullable(reservationArticle));
		given(hashTagRepository.findAllByReservationArticleId(anyLong())).willReturn(List.of(testHashtag));
		given(reservationRepository.findAllByReservationArticleId(anyLong())).willReturn(List.of(testReservation));

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
		assertThat(findReservationArticleResponse.hashtags().get(0).content()).isEqualTo(testHashtag.getContent());
		assertThat(findReservationArticleResponse.dateTimes().get(0).reservationId()).isEqualTo(
			testReservation.getId());
		// TODO 출처가 어디인지 response 에서 확인 필요, 모두 다 reservationArticle 에서 가져와서 비교하는게 아닌 Service 가 하는 일을 보고 맞게 스터빙해서 비교해야함. hashtag, reservation

		// Verify
		verify(reservationArticleRepository, times(1)).findById(anyLong());
		verify(hashTagRepository, times(1)).findAllByReservationArticleId(anyLong());
		verify(reservationRepository, times(1)).findAllByReservationArticleId(anyLong());
	}
}

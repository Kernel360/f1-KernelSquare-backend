package com.kernelsquare.memberapi.domain.reservation_article.dto;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@DisplayName("ReservationArticle 도메인 요청 Dto 단위 테스트")
class ReservationArticleRequestDtoTest {
	ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	Validator validator = factory.getValidator();

	@Test
	@DisplayName("예약창 생성 요청 검증 테스트 - NotNull, NotBlank")
	void whenCreateReservationArticleIsNotBlank_thenValidationFails() {
		CreateReservationArticleRequest createReservationArticleRequest = CreateReservationArticleRequest.builder()
			.title("")
			.content("")
			.introduction("")
			.dateTimes(List.of())
			.hashTags(List.of())
			.build();

		Set<ConstraintViolation<CreateReservationArticleRequest>> violations = validator.validate(
			createReservationArticleRequest);
		Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

		//then
		assertThat(msgList).isEqualTo(Set.of("예약 게시글 소개는 10자 이상 150자 이하로 작성해 주세요.",
			"예약 게시글 제목은 5자 이상 100자 이하로 작성해 주세요.",
			"예약 게시글 소개를 입력해 주세요.",
			"예약 게시글 내용을 입력해 주세요.",
			"예약 게시글 내용은 10자 이상 1000자 이하로 작성해 주세요.",
			"회원 ID를 입력해 주세요.",
			"예약 게시글 제목을 입력해 주세요."));
	}

	@Test
	@DisplayName("예약창 생성 요청 검증 성공 테스트 - Size")
	void whenCreateReservationArticleSizeExceedsLimit_thenValidationSucceeds() {
		CreateReservationArticleRequest createReservationArticleRequest = CreateReservationArticleRequest.builder()
			.memberId(1L)
			.title("a".repeat(100))
			.introduction("a".repeat(100))
			.content("a".repeat(1000))
			.hashTags(List.of())
			.dateTimes(List.of())
			.build();

		Set<ConstraintViolation<CreateReservationArticleRequest>> violations = validator.validate(
			createReservationArticleRequest);
		Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

		assertThat(msgList).isEqualTo(Set.of());
	}

	@Test
	@DisplayName("예약창 생성 요청 검증 실패 테스트 - MaxSize")
	void whenCreateReservationArticleSizeExceedsMaxLimit_thenValidationFails() {
		CreateReservationArticleRequest createReservationArticleRequest = CreateReservationArticleRequest.builder()
			.memberId(1L)
			.title("a".repeat(101))
			.introduction("a".repeat(101))
			.content("a".repeat(1001))
			.hashTags(List.of())
			.dateTimes(List.of())
			.build();

		Set<ConstraintViolation<CreateReservationArticleRequest>> violations = validator.validate(
			createReservationArticleRequest);
		Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

		assertThat(msgList).isEqualTo(
			Set.of("예약 게시글 제목은 5자 이상 100자 이하로 작성해 주세요.", "예약 게시글 내용은 10자 이상 1000자 이하로 작성해 주세요."));
	}

	@Test
	@DisplayName("예약창 생성 요청 검증 실패 테스트 - MinSize")
	void whenCreateReservationArticleSizeExceedsMinLimit_thenValidationFails() {
		CreateReservationArticleRequest createReservationArticleRequest = CreateReservationArticleRequest.builder()
			.memberId(1L)
			.title("a")
			.introduction("")
			.content("a")
			.hashTags(List.of())
			.dateTimes(List.of())
			.build();

		Set<ConstraintViolation<CreateReservationArticleRequest>> violations = validator.validate(
			createReservationArticleRequest);
		Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

		assertThat(msgList).isEqualTo(Set.of("예약 게시글 소개는 10자 이상 150자 이하로 작성해 주세요.",
			"예약 게시글 제목은 5자 이상 100자 이하로 작성해 주세요.",
			"예약 게시글 소개를 입력해 주세요.",
			"예약 게시글 내용은 10자 이상 1000자 이하로 작성해 주세요."));
	}

	@Test
	@DisplayName("예약창 수정 요청 검증 테스트 - NotNull, NotBlank")
	void whenUpdateReservationArticleIsNotBlank_thenValidationFails() {
		UpdateReservationArticleRequest updateReservationArticleRequest = UpdateReservationArticleRequest.builder()
			.title("")
			.introduction("")
			.content("")
			.changeHashtags(List.of())
			.build();

		Set<ConstraintViolation<UpdateReservationArticleRequest>> violations = validator.validate(
			updateReservationArticleRequest);
		Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

		//then
		assertThat(msgList).isEqualTo(Set.of("예약 게시글 소개는 10자 이상 150자 이하로 작성해 주세요.",
			"예약창 제목은 5자 이상 100자 이하로 작성해 주세요.",
			"예약창 내용을 입력해 주세요.",
			"게시글 ID를 입력해 주세요.",
			"예약 게시글 소개를 입력해 주세요.",
			"예약창 내용은 10자 이상 1000자 이하로 작성해 주세요.",
			"예약창 제목을 입력해 주세요.",
			"회원 ID를 입력해 주세요."));
	}

	@Test
	@DisplayName("예약창 수정 요청 검증 성공 테스트 - Size")
	void whenUpdateReservationArticleSizeExceedsLimit_thenValidationSucceeds() {
		UpdateReservationArticleRequest updateReservationArticleRequest = UpdateReservationArticleRequest.builder()
			.memberId(1L)
			.articleId(1L)
			.introduction("a".repeat(100))
			.title("a".repeat(100))
			.content("a".repeat(1000))
			.changeReservations(List.of())
			.changeHashtags(List.of())
			.build();

		Set<ConstraintViolation<UpdateReservationArticleRequest>> violations = validator.validate(
			updateReservationArticleRequest);
		Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

		assertThat(msgList).isEqualTo(Set.of());
	}

	@Test
	@DisplayName("예약창 수정 요청 검증 실패 테스트 - MaxSize")
	void whenUpdateReservationArticleSizeExceedsMaxLimit_thenValidationFails() {
		UpdateReservationArticleRequest updateReservationArticleRequest = UpdateReservationArticleRequest.builder()
			.memberId(1L)
			.introduction("a".repeat(101))
			.changeReservations(List.of())
			.changeHashtags(List.of())
			.title("a".repeat(101))
			.content("a".repeat(1001))
			.build();

		Set<ConstraintViolation<UpdateReservationArticleRequest>> violations = validator.validate(
			updateReservationArticleRequest);
		Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

		assertThat(msgList).isEqualTo(Set.of("예약창 제목은 5자 이상 100자 이하로 작성해 주세요.",
			"게시글 ID를 입력해 주세요.",
			"예약창 내용은 10자 이상 1000자 이하로 작성해 주세요."));
	}

	@Test
	@DisplayName("예약창 수정 요청 검증 실패 테스트 - MinSize")
	void whenUpdateReservationArticleSizeExceedsMinLimit_thenValidationFails() {
		UpdateReservationArticleRequest updateReservationArticleRequest = UpdateReservationArticleRequest.builder()
			.memberId(1L)
			.title("a")
			.introduction("a")
			.content("a")
			.changeHashtags(List.of())
			.changeReservations(List.of())
			.build();

		Set<ConstraintViolation<UpdateReservationArticleRequest>> violations = validator.validate(
			updateReservationArticleRequest);
		Set<String> msgList = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());

		assertThat(msgList).isEqualTo(Set.of("예약 게시글 소개는 10자 이상 150자 이하로 작성해 주세요.",
			"예약창 제목은 5자 이상 100자 이하로 작성해 주세요.",
			"게시글 ID를 입력해 주세요.",
			"예약창 내용은 10자 이상 1000자 이하로 작성해 주세요."));
	}
}
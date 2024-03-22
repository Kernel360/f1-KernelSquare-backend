package com.kernelsquare.memberapi.domain.search.service;

import com.kernelsquare.core.common_response.error.code.CodingMeetingErrorCode;
import com.kernelsquare.core.common_response.error.code.QuestionErrorCode;
import com.kernelsquare.core.common_response.error.code.TechStackErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.core.dto.PageResponse;
import com.kernelsquare.core.dto.Pagination;
import com.kernelsquare.domainmysql.domain.coding_meeting.info.CodingMeetingInfo;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import com.kernelsquare.domainmysql.domain.reservation.repository.ReservationRepository;
import com.kernelsquare.domainmysql.domain.reservation_article.entity.ReservationArticle;
import com.kernelsquare.domainmysql.domain.reservation_article.repository.ReservationArticleRepository;
import com.kernelsquare.domainmysql.domain.search.repository.SearchRepository;
import com.kernelsquare.domainmysql.domain.tech_stack.entity.TechStack;
import com.kernelsquare.memberapi.domain.coding_meeting.dto.CodingMeetingDto;
import com.kernelsquare.memberapi.domain.coding_meeting.mapper.CodingMeetingDtoMapper;
import com.kernelsquare.memberapi.domain.question.dto.FindQuestionResponse;
import com.kernelsquare.memberapi.domain.reservation_article.dto.FindAllReservationArticleResponse;
import com.kernelsquare.memberapi.domain.search.dto.SearchCodingMeetingResponse;
import com.kernelsquare.memberapi.domain.search.dto.SearchQuestionResponse;
import com.kernelsquare.memberapi.domain.search.dto.SearchReservationArticleResponse;
import com.kernelsquare.memberapi.domain.search.dto.SearchTechStackResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
	private final SearchRepository searchRepository;
	private final ReservationArticleRepository reservationArticleRepository;
	private final ReservationRepository reservationRepository;
	private final CodingMeetingDtoMapper codingMeetingDtoMapper;


	public SearchQuestionResponse searchQuestions(Pageable pageable, String keyword) {

		if (keyword.length() > 150) {
			throw new BusinessException(QuestionErrorCode.TOO_LONG_KEYWORD);
		}

		Integer currentPage = pageable.getPageNumber() + 1;

		Page<Question> pages = searchRepository.searchQuestionsByKeyword(pageable, keyword);

		Long totalCount = pages.getTotalElements();

		Integer totalPages = pages.getTotalPages();

		if (totalPages == 0)
			totalPages += 1;

		if (currentPage > totalPages) {
			throw new BusinessException(QuestionErrorCode.PAGE_NOT_FOUND);
		}

		Pagination pagination = Pagination.toEntity(totalPages, pages.getSize(), currentPage.equals(totalPages));

		List<FindQuestionResponse> pageQuestionList = pages.getContent().stream()
			.map(question -> FindQuestionResponse.of(question.getMember(), question, question.getMember().getLevel()))
			.toList();

		PageResponse<FindQuestionResponse> pageResponse = PageResponse.of(pagination, pageQuestionList);

		return SearchQuestionResponse.of(totalCount, pageResponse);
	}

	public SearchTechStackResponse searchTechStacks(Pageable pageable, String keyword) {

		if (keyword.length() > 150) {
			throw new BusinessException(QuestionErrorCode.TOO_LONG_KEYWORD);
		}

		Integer currentPage = pageable.getPageNumber() + 1;

		Page<TechStack> pages = searchRepository.searchTechStacksByKeyword(pageable, keyword);

		Long totalCount = pages.getTotalElements();

		Integer totalPages = pages.getTotalPages();

		if (totalPages == 0)
			totalPages += 1;

		if (currentPage > totalPages) {
			throw new BusinessException(TechStackErrorCode.PAGE_NOT_FOUND);
		}

		Pagination pagination = Pagination.toEntity(totalPages, pages.getSize(), currentPage.equals(totalPages));

		List<String> pageTechStackList = pages.getContent().stream()
			.map(TechStack::getSkill)
			.toList();

		PageResponse<String> pageResponse = PageResponse.of(pagination, pageTechStackList);

		return SearchTechStackResponse.of(totalCount, pageResponse);
	}

	public SearchReservationArticleResponse searchReservationArticles(Pageable pageable, String keyword) {

		if (keyword.length() > 150) {
			throw new BusinessException(QuestionErrorCode.TOO_LONG_KEYWORD);
		}

		Integer currentPage = pageable.getPageNumber() + 1;

		Page<ReservationArticle> pages = searchRepository.searchReservationArticlesByKeyword(pageable, keyword);

		Long totalCount = pages.getTotalElements();

		Integer totalPages = pages.getTotalPages();

		if (totalPages == 0)
			totalPages += 1;

		if (currentPage > totalPages) {
			throw new BusinessException(TechStackErrorCode.PAGE_NOT_FOUND);
		}

		Pagination pagination = Pagination.toEntity(totalPages, pages.getSize(), currentPage.equals(totalPages));

		List<FindAllReservationArticleResponse> pageReservationArticleList = pages.getContent().stream()
			.map(article -> {
				Long coffeeChatCount = reservationArticleRepository.countAllByMemberIdAndEndTimeBefore(
					article.getMember().getId(),
					LocalDateTime.now());
				Long availableReservationCount = reservationRepository.countByReservationArticleIdAndMemberIdIsNull(
					article.getId());
				Long totalReservationCount = reservationRepository.countAllByReservationArticleId(article.getId());
				Boolean articleStatus = checkIfReservationWithinTheAvailablePeriod(article.getStartTime());
				return FindAllReservationArticleResponse.of(
					article.getMember(),
					article,
					articleStatus,
					coffeeChatCount,
					availableReservationCount
					, totalReservationCount
				);
			})
			.toList();

		PageResponse<FindAllReservationArticleResponse> pageResponse = PageResponse.of(pagination, pageReservationArticleList);

		return SearchReservationArticleResponse.of(totalCount, pageResponse);
	}

	public SearchCodingMeetingResponse searchCodingMeetings(Pageable pageable, String filter, String keyword) {

		if (keyword.length() > 150) {
			throw new BusinessException(QuestionErrorCode.TOO_LONG_KEYWORD);
		}

		Integer currentPage = pageable.getPageNumber() + 1;

		Page<CodingMeetingInfo.ListInfo> pages = searchRepository.searchCodingMeetingsByKeyword(pageable, filter, keyword);

		Long totalCount = pages.getTotalElements();

		Integer totalPages = pages.getTotalPages();

		if (totalPages == 0)
			totalPages += 1;

		if (currentPage > totalPages) {
			throw new BusinessException(TechStackErrorCode.PAGE_NOT_FOUND);
		}

		List<CodingMeetingDto.FindAllResponse> pageCodingMeetingList = pages.getContent().stream()
			.map(info -> codingMeetingDtoMapper.toFindAllResponse(info))
			.toList();

		PageResponse<CodingMeetingDto.FindAllResponse> pageResponse = PageResponse.of(pageable, pages, pageCodingMeetingList);

		return SearchCodingMeetingResponse.of(totalCount, pageResponse);
	}

	private Boolean checkIfReservationWithinTheAvailablePeriod(LocalDateTime startTime) {
		boolean check = false;
		LocalDateTime currentTime = LocalDateTime.now();

		LocalDate minStartDate = startTime.toLocalDate();
		LocalDate currentDate = currentTime.toLocalDate();

		if (currentDate.isAfter(minStartDate.minusDays(7)) && currentDate.isBefore(minStartDate.minusDays(1))) {
			check = true;
		}
		return check;
	}
}

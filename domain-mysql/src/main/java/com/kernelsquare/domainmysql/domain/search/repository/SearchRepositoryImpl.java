package com.kernelsquare.domainmysql.domain.search.repository;

import com.kernelsquare.core.common_response.error.code.CodingMeetingErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.coding_meeting.entity.CodingMeeting;
import com.kernelsquare.domainmysql.domain.coding_meeting.entity.QCodingMeeting;
import com.kernelsquare.domainmysql.domain.coding_meeting.entity.QCodingMeetingHashtag;
import com.kernelsquare.domainmysql.domain.coding_meeting.entity.QCodingMeetingLocation;
import com.kernelsquare.domainmysql.domain.coding_meeting.info.CodingMeetingInfo;
import com.kernelsquare.domainmysql.domain.hashtag.entity.QHashtag;
import com.kernelsquare.domainmysql.domain.question.entity.QQuestion;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import com.kernelsquare.domainmysql.domain.question_tech_stack.entity.QQuestionTechStack;
import com.kernelsquare.domainmysql.domain.reservation_article.entity.QReservationArticle;
import com.kernelsquare.domainmysql.domain.reservation_article.entity.ReservationArticle;
import com.kernelsquare.domainmysql.domain.tech_stack.entity.QTechStack;
import com.kernelsquare.domainmysql.domain.tech_stack.entity.TechStack;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SearchRepositoryImpl implements SearchRepository {
	private final JPAQueryFactory queryFactory;

	@Override
	public Page<Question> searchQuestionsByKeyword(Pageable pageable, String keyword) {
		QQuestion question = QQuestion.question;
		QQuestionTechStack questionTechStack = QQuestionTechStack.questionTechStack;

		BooleanBuilder builder = new BooleanBuilder();
		if (keyword != null && !keyword.isEmpty()) {
			String keywordLowerCase = keyword.toLowerCase();

			builder.or(question.title.toLowerCase().contains(keywordLowerCase))
				.or(question.content.toLowerCase().contains(keywordLowerCase))
				.or(question.techStackList.any().techStack.skill.toLowerCase().in(keywordLowerCase))
			;
		}

		List<Question> content = queryFactory
			.selectFrom(question)
			.leftJoin(question.techStackList, questionTechStack)
			.where(builder)
			.orderBy(question.createdDate.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(question.count())
			.from(question)
			.where(builder);

		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
	}

	@Override
	public Page<TechStack> searchTechStacksByKeyword(Pageable pageable, String keyword) {
		QTechStack techStack = QTechStack.techStack;

		BooleanBuilder builder = new BooleanBuilder();
		if (keyword != null && !keyword.isEmpty()) {
			String keywordLowerCase = keyword.toLowerCase();

			builder.or(techStack.skill.toLowerCase().contains(keywordLowerCase));
		}

		List<TechStack> content = queryFactory
			.selectFrom(techStack)
			.where(builder)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(techStack.count())
			.from(techStack)
			.where(builder);

		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
	}

	@Override
	public Page<ReservationArticle> searchReservationArticlesByKeyword(Pageable pageable, String keyword) {
		QReservationArticle reservationArticle = QReservationArticle.reservationArticle;
		QHashtag hashtag = QHashtag.hashtag;

		BooleanBuilder builder = new BooleanBuilder();
		if (keyword != null && !keyword.isEmpty()) {
			String keywordLowerCase = keyword.toLowerCase();

			builder.or(reservationArticle.title.toLowerCase().contains(keywordLowerCase))
				.or(reservationArticle.hashtagList.any().hashtag.content.toLowerCase().in(keywordLowerCase));
		}

		List<ReservationArticle> content = queryFactory
			.selectFrom(reservationArticle)
			.leftJoin(reservationArticle.hashtagList, hashtag)
			.where(builder)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(reservationArticle.count())
			.from(reservationArticle)
			.where(builder);

		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
	}

	@Override
	public Page<CodingMeetingInfo.ListInfo> searchCodingMeetingsByKeyword(Pageable pageable, String filter, String keyword) {
		QCodingMeeting codingMeeting = QCodingMeeting.codingMeeting;
		QCodingMeetingHashtag codingMeetingHashtag = QCodingMeetingHashtag.codingMeetingHashtag;
		QCodingMeetingLocation codingMeetingLocation = QCodingMeetingLocation.codingMeetingLocation;

		BooleanBuilder builder = new BooleanBuilder();
		if (keyword != null && !keyword.isEmpty()) {
			String keywordLowerCase = keyword.toLowerCase();

			switch (filter) {
				case "all" ->
					builder.or(codingMeeting.codingMeetingTitle.toLowerCase().contains(keywordLowerCase))
						.or(codingMeeting.codingMeetingLocation.codingMeetingLocationPlaceName.toLowerCase().in(keywordLowerCase))
						.or(codingMeeting.codingMeetingHashtags.any().codingMeetingHashtag.codingMeetingHashtagContent.toLowerCase().in(keywordLowerCase));
				case "open" ->
					builder.and(codingMeeting.codingMeetingClosed.eq(Boolean.FALSE))
						.or(codingMeeting.codingMeetingTitle.toLowerCase().contains(keywordLowerCase))
						.or(codingMeeting.codingMeetingLocation.codingMeetingLocationPlaceName.toLowerCase().in(keywordLowerCase))
						.or(codingMeeting.codingMeetingHashtags.any().codingMeetingHashtag.codingMeetingHashtagContent.toLowerCase().in(keywordLowerCase));
				case "closed" ->
					builder.and(codingMeeting.codingMeetingClosed.eq(Boolean.TRUE))
						.or(codingMeeting.codingMeetingTitle.toLowerCase().contains(keywordLowerCase))
						.or(codingMeeting.codingMeetingLocation.codingMeetingLocationPlaceName.toLowerCase().in(keywordLowerCase))
						.or(codingMeeting.codingMeetingHashtags.any().codingMeetingHashtag.codingMeetingHashtagContent.toLowerCase().in(keywordLowerCase));
				default -> throw new BusinessException(CodingMeetingErrorCode.FILTER_PARAMETER_NOT_VALID);
			}

		}

		List<CodingMeeting> content = queryFactory
			.selectFrom(codingMeeting)
			.leftJoin(codingMeeting.codingMeetingLocation, codingMeetingLocation)
			.leftJoin(codingMeeting.codingMeetingHashtags, codingMeetingHashtag)
			.where(builder)
			.orderBy(codingMeeting.createdDate.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		List<CodingMeetingInfo.ListInfo> contentInfo = content.stream()
			.map(CodingMeetingInfo.ListInfo::of)
			.toList();

		JPAQuery<Long> countQuery = queryFactory
			.select(codingMeeting.count())
			.from(codingMeeting)
			.where(builder);

		return PageableExecutionUtils.getPage(contentInfo, pageable, countQuery::fetchOne);
	}
}

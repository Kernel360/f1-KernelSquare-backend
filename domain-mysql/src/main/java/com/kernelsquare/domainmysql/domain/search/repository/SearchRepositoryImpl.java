package com.kernelsquare.domainmysql.domain.search.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.kernelsquare.domainmysql.domain.question.entity.QQuestion;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import com.kernelsquare.domainmysql.domain.question_tech_stack.entity.QQuestionTechStack;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository

public class SearchRepositoryImpl extends QuerydslRepositorySupport implements SearchRepository {
	private final JPAQueryFactory queryFactory;

	public SearchRepositoryImpl(JPAQueryFactory queryFactory) {
		super(Question.class);
		this.queryFactory = queryFactory;
	}

	@Override
	public Page<Question> searchQuestionsByKeyword(Pageable pageable, String keyword) {
		QQuestion question = QQuestion.question;
		QQuestionTechStack questionTechStack = QQuestionTechStack.questionTechStack;

		BooleanBuilder builder = new BooleanBuilder();
		if (keyword != null && !keyword.isEmpty()) {
			String keywordLowerCase = keyword.toLowerCase();

			builder.or(question.title.toLowerCase().contains(keywordLowerCase)
				.or(question.content.toLowerCase().contains(keywordLowerCase))
				.or(question.techStackList.any().techStack.skill.toLowerCase().in(keywordLowerCase))
			);
		}

		List<Question> content = queryFactory
			.selectFrom(question)
			.leftJoin(question.techStackList, questionTechStack)
			.where(builder)
			.orderBy(question.createdDate.desc())
			.distinct()
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = queryFactory
			.select(question.count())
			.from(question)
			.where(builder);

		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
	}
}

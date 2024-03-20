package com.kernelsquare.domainmysql.domain.question.repository;

import com.kernelsquare.domainmysql.domain.level.entity.QLevel;
import com.kernelsquare.domainmysql.domain.member.entity.QMember;
import com.kernelsquare.domainmysql.domain.question.dto.FindAllQuestions;
import com.kernelsquare.domainmysql.domain.question.dto.QFindAllQuestions;
import com.kernelsquare.domainmysql.domain.question.entity.QQuestion;
import com.kernelsquare.domainmysql.domain.question_tech_stack.entity.QQuestionTechStack;
import com.kernelsquare.domainmysql.domain.tech_stack.entity.QTechStack;
import com.querydsl.core.types.dsl.Expressions;
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
public class QuestionQuerydslRepositoryImpl implements QuestionQuerydslRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<FindAllQuestions> findAllQuestions(Pageable pageable) {
        QQuestion question = QQuestion.question;
        QMember member = QMember.member;
        QLevel level = QLevel.level;
        QQuestionTechStack questionTechStack = QQuestionTechStack.questionTechStack;
        QTechStack techStack = QTechStack.techStack;

        List<FindAllQuestions> content = queryFactory
            .select(
                new QFindAllQuestions(
                    question.id,
                    question.title,
                    question.imageUrl,
                    question.viewCount,
                    question.closedStatus,
                    member.id,
                    member.nickname,
                    member.imageUrl,
                    level.name,
                    level.imageUrl,
                    question.createdDate,
                    question.modifiedDate,
                    Expressions.stringTemplate("GROUP_CONCAT({0})", techStack.skill)
                )
            )
            .from(question)
            .leftJoin(question.member, member)
            .leftJoin(member.level, level)
            .leftJoin(question.techStackList, questionTechStack)
            .leftJoin(questionTechStack.techStack, techStack)
            .groupBy(question.id)
            .orderBy(question.createdDate.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(question.count())
            .from(question);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
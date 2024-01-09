package com.kernel360.kernelsquare.domain.search.repository;

import com.kernel360.kernelsquare.domain.question.entity.QQuestion;
import com.kernel360.kernelsquare.domain.question.entity.Question;
import com.kernel360.kernelsquare.domain.question_tech_stack.entity.QQuestionTechStack;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CustomQuestionRepositoryImpl implements CustomQuestionRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Question> searchByKeyword(String keyword) {
        QQuestion question = QQuestion.question;
        QQuestionTechStack questionTechStack = QQuestionTechStack.questionTechStack;

        BooleanBuilder builder = new BooleanBuilder();
        if (keyword != null && !keyword.isEmpty()) {
            builder.and(question.title.containsIgnoreCase(keyword)
                .or(question.content.containsIgnoreCase(keyword)));
        }
        if (keyword != null && !keyword.isEmpty()) {
            builder.and(questionTechStack.techStack.skill.in(keyword));
        }

        List<Question> result = queryFactory
            .selectFrom(question)
            .leftJoin(question.techStackList, questionTechStack)
            .where(builder)
            .distinct()
            .fetch();

        return result;
    }
}

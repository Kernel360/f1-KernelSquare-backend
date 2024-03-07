package com.kernelsquare.memberapi.domain.answer.validation;

import com.kernelsquare.core.common_response.error.code.AnswerErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.question.entity.Question;

import java.util.Objects;

public class AnswerValidation {
    public static void validateQuestionAuthorEqualsAnswerAuthor(Question question, Member member) {
        if (Objects.equals(question.getMember().getId(), member.getId())) {
            throw new BusinessException(AnswerErrorCode.ANSWER_SELF_IMPOSSIBLE);
        }
    }
}

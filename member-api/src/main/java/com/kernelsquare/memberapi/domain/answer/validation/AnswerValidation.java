package com.kernelsquare.memberapi.domain.answer.validation;

import com.kernelsquare.core.common_response.error.code.AnswerErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.answer.entity.Answer;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdapter;

import java.util.Objects;

public class AnswerValidation {
    public static void validateQuestionAuthorEqualsAnswerAuthor(Question question, Member member) {
        if (Objects.equals(question.getMember().getId(), member.getId())) {
            throw new BusinessException(AnswerErrorCode.ANSWER_SELF_IMPOSSIBLE);
        }
    }

    public static void validateUpdatePermission(MemberAdapter memberAdapter, Answer answer) {
        if (!memberAdapter.getMember().getId().equals(answer.getMember().getId())) {
            throw new BusinessException(AnswerErrorCode.ANSWER_UPDATE_NOT_AUTHORIZED);
        }
    }

    public static void validateDeletePermission(MemberAdapter memberAdapter, Answer answer) {
        if (!memberAdapter.getMember().getId().equals(answer.getMember().getId())) {
            throw new BusinessException(AnswerErrorCode.ANSWER_DELETE_NOT_AUTHORIZED);
        }
    }
}

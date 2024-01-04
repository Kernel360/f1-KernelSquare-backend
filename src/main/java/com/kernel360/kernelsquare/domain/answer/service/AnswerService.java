package com.kernel360.kernelsquare.domain.answer.service;

import com.kernel360.kernelsquare.domain.answer.dto.CreateAnswerRequest;
import com.kernel360.kernelsquare.domain.answer.dto.FindAnswerResponse;
import com.kernel360.kernelsquare.domain.answer.dto.UpdateAnswerRequest;
import com.kernel360.kernelsquare.domain.answer.entity.Answer;
import com.kernel360.kernelsquare.domain.answer.repository.AnswerRepository;
import com.kernel360.kernelsquare.domain.image.utils.ImageUtils;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.member.repository.MemberRepository;
import com.kernel360.kernelsquare.domain.question.entity.Question;
import com.kernel360.kernelsquare.domain.question.repository.QuestionRepository;
import com.kernel360.kernelsquare.global.common_response.error.code.AnswerErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.code.MemberErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.code.QuestionErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;

    @Transactional(readOnly = true)
    public List<FindAnswerResponse> findAllAnswer(Long questionId) {
        return answerRepository.findAnswersByQuestionIdSortedByCreationDate(questionId)
                .stream()
                .map(FindAnswerResponse::from)
                .toList();
    }

    @Transactional
    public Long createAnswer(CreateAnswerRequest createAnswerRequest, Long questionId) {
        Member member = memberRepository.findById(createAnswerRequest.memberId())
                .orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new BusinessException(QuestionErrorCode.QUESTION_NOT_FOUND));
        Answer answer = CreateAnswerRequest.toEntity(createAnswerRequest, question, member);
        answerRepository.save(answer);
        return answer.getId();
    }

    @Transactional
    public void updateAnswer(UpdateAnswerRequest updateAnswerRequest, Long answerId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new BusinessException(AnswerErrorCode.ANSWER_NOT_FOUND));

        answer.update(updateAnswerRequest.content(), ImageUtils.parseFilePath(updateAnswerRequest.imageUrl()));
    }

    @Transactional
    public void deleteAnswer(Long answerId) {
        answerRepository.deleteById(answerId);
    }
}

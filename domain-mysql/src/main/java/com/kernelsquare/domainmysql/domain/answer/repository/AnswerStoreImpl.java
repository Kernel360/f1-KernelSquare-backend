package com.kernelsquare.domainmysql.domain.answer.repository;

import com.kernelsquare.domainmysql.domain.answer.entity.Answer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnswerStoreImpl implements AnswerStore {
    private final AnswerRepository answerRepository;

    @Override
    public void upVote(Long answerId) {
        answerRepository.upVoteAnswer(answerId);
    }

    @Override
    public void downVote(Long answerId) {
        answerRepository.downVoteAnswer(answerId);
    }

    @Override
    public Answer store(Answer answer) {
        return answerRepository.save(answer);
    }

    @Override
    public void delete(Long answerId) {
        answerRepository.deleteById(answerId);
    }
}

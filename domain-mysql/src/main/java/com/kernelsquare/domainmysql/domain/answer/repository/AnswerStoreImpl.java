package com.kernelsquare.domainmysql.domain.answer.repository;

import org.springframework.stereotype.Component;

import com.kernelsquare.domainmysql.domain.answer.entity.Answer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AnswerStoreImpl implements AnswerStore{
	private final AnswerRepository answerRepository;

	@Override
	public Answer store(Answer initAnswer) {
		return answerRepository.save(initAnswer);
	}
}

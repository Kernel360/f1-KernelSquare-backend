package com.kernelsquare.adminapi.domain.answer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kernelsquare.adminapi.domain.answer.dto.FindAllAnswerResponse;
import com.kernelsquare.adminapi.domain.answer.dto.FindAnswerResponse;
import com.kernelsquare.domainmysql.domain.answer.entity.Answer;
import com.kernelsquare.domainmysql.domain.answer.repository.AnswerRepository;
import com.kernelsquare.domainmysql.domain.member_answer_vote.entity.MemberAnswerVote;
import com.kernelsquare.domainmysql.domain.member_answer_vote.repository.MemberAnswerVoteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerService {
	private final AnswerRepository answerRepository;
	private final MemberAnswerVoteRepository memberAnswerVoteRepository;

	@Transactional(readOnly = true)
	public FindAllAnswerResponse findAllAnswer(Long questionId) {
		List<FindAnswerResponse> result = new ArrayList<>();
		if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
			Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
			List<MemberAnswerVote> voteList = memberAnswerVoteRepository.findAllByMemberId(memberId);
			Map<Long, Integer> voteStatusMap = voteList.stream()
				.collect(Collectors.toMap(MemberAnswerVote::getAnswerId, MemberAnswerVote::getStatus));
			List<Answer> answerList = answerRepository.findAnswersByQuestionIdSortedByCreationDate(questionId);
			for (Answer answer : answerList) {
				if (voteStatusMap.containsKey(answer.getId())) {
					result.add(FindAnswerResponse.from(answer, null, answer.getMember().getLevel().getName(),
						Long.valueOf(voteStatusMap.get(answer.getId()))));
				} else {
					result.add(FindAnswerResponse.from(answer, null, answer.getMember().getLevel().getName(),
						Long.valueOf("0")));
				}
			}
			return FindAllAnswerResponse.from(result);
		}
		List<Answer> answerList = answerRepository.findAnswersByQuestionIdSortedByCreationDate(questionId);
		for (Answer answer : answerList) {
			result.add(FindAnswerResponse.from(answer, null, answer.getMember().getLevel().getName(),
				Long.valueOf("0")));
		}
		return FindAllAnswerResponse.from(result);
	}

	@Transactional
	public void deleteAnswer(Long answerId) {
		answerRepository.deleteById(answerId);
	}
}

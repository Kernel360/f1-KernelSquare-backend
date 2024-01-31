package com.kernelsquare.memberapi.domain.answer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kernelsquare.core.common_response.error.code.AnswerErrorCode;
import com.kernelsquare.core.common_response.error.code.LevelErrorCode;
import com.kernelsquare.core.common_response.error.code.MemberErrorCode;
import com.kernelsquare.core.common_response.error.code.QuestionErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.core.util.ExperiencePolicy;
import com.kernelsquare.domainmysql.domain.answer.entity.Answer;
import com.kernelsquare.domainmysql.domain.answer.repository.AnswerRepository;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.level.repository.LevelRepository;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberRepository;
import com.kernelsquare.domainmysql.domain.member_answer_vote.entity.MemberAnswerVote;
import com.kernelsquare.domainmysql.domain.member_answer_vote.repository.MemberAnswerVoteRepository;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import com.kernelsquare.domainmysql.domain.question.repository.QuestionRepository;
import com.kernelsquare.memberapi.domain.answer.dto.CreateAnswerRequest;
import com.kernelsquare.memberapi.domain.answer.dto.FindAllAnswerResponse;
import com.kernelsquare.memberapi.domain.answer.dto.FindAnswerResponse;
import com.kernelsquare.memberapi.domain.answer.dto.UpdateAnswerRequest;
import com.kernelsquare.memberapi.domain.image.utils.ImageUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerService {
	private final AnswerRepository answerRepository;
	private final MemberRepository memberRepository;
	private final QuestionRepository questionRepository;
	private final MemberAnswerVoteRepository memberAnswerVoteRepository;
	private final LevelRepository levelRepository;

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
	public Long createAnswer(CreateAnswerRequest createAnswerRequest, Long questionId) {
		Member member = memberRepository.findById(createAnswerRequest.memberId())
			.orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));
		Question question = questionRepository.findById(questionId)
			.orElseThrow(() -> new BusinessException(QuestionErrorCode.QUESTION_NOT_FOUND));
		Answer answer = CreateAnswerRequest.toEntity(createAnswerRequest, question, member);
		answerRepository.save(answer);
		member.addExperience(ExperiencePolicy.MEMBER_DAILY_ATTENDED.getReward());
		if (member.isExperienceExceed(member.getExperience())) {
			member.updateExperience(member.getExperience() - member.getLevel().getLevelUpperLimit());
			Level nextLevel = levelRepository.findByName(member.getLevel().getName() + 1)
				.orElseThrow(() -> new BusinessException(LevelErrorCode.LEVEL_NOT_FOUND));
			member.updateLevel(nextLevel);
		}
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

package com.kernelsquare.memberapi.domain.answer.service;

import com.kernelsquare.core.common_response.error.code.AnswerErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.core.util.ExperiencePolicy;
import com.kernelsquare.core.util.ImageUtils;
import com.kernelsquare.domainmongodb.domain.alert.entity.Alert;
import com.kernelsquare.domainmongodb.domain.alert.repository.AlertStore;
import com.kernelsquare.domainmysql.domain.answer.command.AnswerCommand;
import com.kernelsquare.domainmysql.domain.answer.entity.Answer;
import com.kernelsquare.domainmysql.domain.answer.repository.AnswerReader;
import com.kernelsquare.domainmysql.domain.answer.repository.AnswerRepository;
import com.kernelsquare.domainmysql.domain.answer.repository.AnswerStore;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.level.repository.LevelReader;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member_answer_vote.entity.MemberAnswerVote;
import com.kernelsquare.domainmysql.domain.member_answer_vote.repository.MemberAnswerVoteReader;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import com.kernelsquare.domainmysql.domain.question.repository.QuestionReader;
import com.kernelsquare.memberapi.domain.alert.manager.SseManager;
import com.kernelsquare.memberapi.domain.answer.dto.FindAllAnswerResponse;
import com.kernelsquare.memberapi.domain.answer.dto.FindAnswerResponse;
import com.kernelsquare.memberapi.domain.answer.dto.UpdateAnswerRequest;
import com.kernelsquare.memberapi.domain.answer.validation.AnswerValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerService {
	private final AnswerRepository answerRepository;
	private final MemberAnswerVoteReader memberAnswerVoteReader;
	private final AnswerReader answerReader;
	private final AnswerStore answerStore;
	private final QuestionReader questionReader;
	private final LevelReader levelReader;
	private final SseManager sseManager;
	private final AlertStore alertStore;

	@Transactional(readOnly = true)
	public FindAllAnswerResponse findAllAnswer(Long questionId) {
		List<FindAnswerResponse> result = new ArrayList<>();
		if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
			Long memberId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
			List<MemberAnswerVote> voteList = memberAnswerVoteReader.findAllByMemberId(memberId);
			Map<Long, Integer> voteStatusMap = voteList.stream()
				.collect(Collectors.toMap(MemberAnswerVote::getAnswerId, MemberAnswerVote::getStatus));
			List<Answer> answerList = answerReader.findAnswers(questionId);
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
		List<Answer> answerList = answerReader.findAnswers(questionId);
		for (Answer answer : answerList) {
			result.add(FindAnswerResponse.from(answer, null, answer.getMember().getLevel().getName(),
				Long.valueOf("0")));
		}
		return FindAllAnswerResponse.from(result);
	}

	@Transactional
	public Long createAnswer(AnswerCommand.CreateAnswer command) {
		Member member = command.author();

		Question question = questionReader.findQuestion(command.questionId());

		AnswerValidation.validateQuestionAuthorEqualsAnswerAuthor(question, member);

		Answer answer = command.toEntity(question);

		answerStore.store(answer);
		member.addExperience(ExperiencePolicy.MEMBER_DAILY_ATTENDED.getReward());
		if (member.isExperienceExceed(member.getExperience())) {
			member.updateExperience(member.getExperience() - member.getLevel().getLevelUpperLimit());
			Level nextLevel = levelReader.findLevel(member.getLevel().getName() + 1);
			member.updateLevel(nextLevel);
		}

		String message = createMessage(question, member);

		Alert alert = Alert.builder()
			.memberId(question.getMember().getId().toString())
			.message(message)
			.alertType(Alert.AlertType.QUESTION_REPLY)
			.build();

		alertStore.store(alert);

		sseManager.send(question.getMember(), message, Alert.AlertType.QUESTION_REPLY);

		return answer.getId();
	}

	private String createMessage(Question question, Member member) {
		return member.getNickname() + "님이 " + question.getTitle() + " 글에 답변이 달렸습니다.";
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

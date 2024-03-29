package com.kernelsquare.memberapi.domain.question.service;

import com.kernelsquare.core.common_response.error.code.LevelErrorCode;
import com.kernelsquare.core.common_response.error.code.MemberErrorCode;
import com.kernelsquare.core.common_response.error.code.QuestionErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.core.dto.PageResponse;
import com.kernelsquare.core.dto.Pagination;
import com.kernelsquare.core.util.ExperiencePolicy;
import com.kernelsquare.core.util.ImageUtils;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.level.repository.LevelRepository;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberRepository;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import com.kernelsquare.domainmysql.domain.question.info.QuestionInfo;
import com.kernelsquare.domainmysql.domain.question.repository.QuestionReader;
import com.kernelsquare.domainmysql.domain.question.repository.QuestionRepository;
import com.kernelsquare.domainmysql.domain.question_tech_stack.entity.QuestionTechStack;
import com.kernelsquare.domainmysql.domain.question_tech_stack.repository.QuestionTechStackRepository;
import com.kernelsquare.domainmysql.domain.tech_stack.entity.TechStack;
import com.kernelsquare.domainmysql.domain.tech_stack.repository.TechStackRepository;
import com.kernelsquare.memberapi.domain.question.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {
	private final QuestionRepository questionRepository;
	private final MemberRepository memberRepository;
	private final TechStackRepository techStackRepository;
	private final QuestionTechStackRepository questionTechStackRepository;
	private final LevelRepository levelRepository;
	private final QuestionReader questionReader;

	@Transactional
	public CreateQuestionResponse createQuestion(CreateQuestionRequest createQuestionRequest) {
		Member member = memberRepository.findById(createQuestionRequest.memberId())
			.orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));

		Question question = CreateQuestionRequest.toEntity(createQuestionRequest, member);
		Question saveQuestion = questionRepository.save(question);

		List<String> skills = createQuestionRequest.skills();
		List<QuestionTechStack> techStackList = new ArrayList<>();

		saveTechStackList(question, skills, techStackList);

		member.addExperience(ExperiencePolicy.QUESTION_CREATED.getReward());
		if (member.isExperienceExceed(member.getExperience())) {
			member.updateExperience(member.getExperience() - member.getLevel().getLevelUpperLimit());
			Level nextLevel = levelRepository.findByName(member.getLevel().getName() + 1)
				.orElseThrow(() -> new BusinessException(LevelErrorCode.LEVEL_NOT_FOUND));
			member.updateLevel(nextLevel);
		}
		return CreateQuestionResponse.from(saveQuestion);
	}

	@Transactional(readOnly = true)
	public FindQuestionResponse findQuestion(
		Long questionId
	) {
		Question question = questionRepository.findById(questionId)
			.orElseThrow(() -> new BusinessException(QuestionErrorCode.QUESTION_NOT_FOUND));

		Member member = question.getMember();

		return FindQuestionResponse.of(member, question, member.getLevel());
	}

	@Transactional(readOnly = true)
	public PageResponse<QuestionDto.FindAllResponse> findAllQuestions(Pageable pageable) {

		Integer currentPage = pageable.getPageNumber() + 1;

		Page<QuestionInfo.FindAllQuestionsInfo> QuestionPages = questionReader.findAllQuestions(pageable);

		Integer totalPages = QuestionPages.getTotalPages();

		if (totalPages == 0)
			totalPages += 1;

		if (currentPage > totalPages) {
			throw new BusinessException(QuestionErrorCode.PAGE_NOT_FOUND);
		}

		Pagination pagination = Pagination.toEntity(totalPages, QuestionPages.getSize(), currentPage.equals(totalPages));

		List<QuestionDto.FindAllResponse> QuestionPagesResponse = QuestionPages.getContent().stream()
			.map(QuestionDto.FindAllResponse::of)
			.toList();

		return PageResponse.of(pagination, QuestionPagesResponse);
	}

	@Transactional(readOnly = true)
	public FindAllQuestionResponse findAllQuestionsSeo() {
		return FindAllQuestionResponse.of(questionRepository.findAll().stream()
				.map(Question::getId)
				.map(FindQuestionIdResponse::of)
				.toList());
	}

	@Transactional
	public void updateQuestion(Long questionId, UpdateQuestionRequest updateQuestionRequest) {
		Question question = questionRepository.findById(questionId)
			.orElseThrow(() -> new BusinessException(QuestionErrorCode.QUESTION_NOT_FOUND));

		question.update(updateQuestionRequest.title(), updateQuestionRequest.content(),
			ImageUtils.parseFilePath(updateQuestionRequest.imageUrl()));

		List<String> skills = updateQuestionRequest.skills();
		List<QuestionTechStack> techStackList = new ArrayList<>();

		questionTechStackRepository.deleteAllByQuestionId(questionId);

		saveTechStackList(question, skills, techStackList);
	}

	private void saveTechStackList(Question question, List<String> skills, List<QuestionTechStack> techStackList) {
		for (String skill : skills) {
			TechStack techStack = techStackRepository.findBySkill(skill).orElseThrow(() -> new RuntimeException());

			QuestionTechStack questionTechStack = QuestionTechStack.builder()
				.techStack(techStack)
				.question(question)
				.build();

			questionTechStackRepository.save(questionTechStack);
			techStackList.add(questionTechStack);
		}
		question.setTechStackList(techStackList);
	}

	@Transactional
	public void deleteQuestion(Long questionId) {
		questionRepository.findById(questionId)
			.orElseThrow(() -> new BusinessException(QuestionErrorCode.QUESTION_NOT_FOUND));

		questionRepository.deleteById(questionId);

		questionTechStackRepository.deleteAllByQuestionId(questionId);
	}
}

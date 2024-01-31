package com.kernelsquare.adminapi.domain.question.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kernelsquare.adminapi.domain.question.dto.FindQuestionResponse;
import com.kernelsquare.core.common_response.error.code.QuestionErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.core.dto.PageResponse;
import com.kernelsquare.core.dto.Pagination;
import com.kernelsquare.domainmysql.domain.level.repository.LevelRepository;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.repository.MemberRepository;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import com.kernelsquare.domainmysql.domain.question.repository.QuestionRepository;
import com.kernelsquare.domainmysql.domain.question_tech_stack.repository.QuestionTechStackRepository;
import com.kernelsquare.domainmysql.domain.tech_stack.repository.TechStackRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {
	private final QuestionRepository questionRepository;
	private final MemberRepository memberRepository;
	private final TechStackRepository techStackRepository;
	private final QuestionTechStackRepository questionTechStackRepository;
	private final LevelRepository levelRepository;

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
	public PageResponse<FindQuestionResponse> findAllQuestions(Pageable pageable) {

		Integer currentPage = pageable.getPageNumber() + 1;

		Page<Question> pages = questionRepository.findAll(pageable);

		Integer totalPages = pages.getTotalPages();

		if (totalPages == 0)
			totalPages += 1;

		if (currentPage > totalPages) {
			throw new BusinessException(QuestionErrorCode.PAGE_NOT_FOUND);
		}

		Pagination pagination = Pagination.toEntity(totalPages, pages.getSize(), currentPage.equals(totalPages));

		List<FindQuestionResponse> responsePages = pages.getContent().stream()
			.map(Question::getId)
			.map(this::findQuestion)
			.toList();

		return PageResponse.of(pagination, responsePages);
	}

	@Transactional
	public void deleteQuestion(Long questionId) {
		questionRepository.findById(questionId)
			.orElseThrow(() -> new BusinessException(QuestionErrorCode.QUESTION_NOT_FOUND));

		questionRepository.deleteById(questionId);

		questionTechStackRepository.deleteAllByQuestionId(questionId);
	}
}

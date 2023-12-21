package com.kernel360.kernelsquare.domain.question.service;

import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.member.repository.MemberRepository;
import com.kernel360.kernelsquare.domain.question.dto.CreateQuestionRequest;
import com.kernel360.kernelsquare.domain.question.dto.FindQuestionResponse;
import com.kernel360.kernelsquare.domain.question.dto.PutQuestionRequest;
import com.kernel360.kernelsquare.domain.question.entity.Question;
import com.kernel360.kernelsquare.domain.question.repository.QuestionRepository;
import com.kernel360.kernelsquare.domain.question_tech_stack.entity.QuestionTechStack;
import com.kernel360.kernelsquare.domain.question_tech_stack.repository.QuestionTechStackRepository;
import com.kernel360.kernelsquare.domain.tech_stack.entity.TechStack;
import com.kernel360.kernelsquare.domain.tech_stack.repository.TechStackRepository;
import com.kernel360.kernelsquare.global.common_response.error.code.MemberErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.code.QuestionErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;
import com.kernel360.kernelsquare.global.dto.PageResponse;
import com.kernel360.kernelsquare.global.dto.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;
    private final TechStackRepository techStackRepository;
    private final QuestionTechStackRepository questionTechStackRepository;

    @Transactional
    public Long createQuestion(CreateQuestionRequest createQuestionRequest) {
        Member member = memberRepository.findById(createQuestionRequest.memberId())
            .orElseThrow(() -> new BusinessException(MemberErrorCode.MEMBER_NOT_FOUND));

        Question question = CreateQuestionRequest.toEntity(createQuestionRequest, member);

        questionRepository.save(question);

        List<String> skills = createQuestionRequest.skills();
        List<QuestionTechStack> techStackList = new ArrayList<>();

        saveTechStackList(question, skills, techStackList);

        return question.getId();
    }

    @Transactional(readOnly = true)
    public FindQuestionResponse findQuestion(
        Long questionId
    ) {
        Question question = questionRepository.findById(questionId)
            .orElseThrow(() -> new BusinessException(QuestionErrorCode.NOT_FOUND_QUESTION));

        Member member = question.getMember();

        return FindQuestionResponse.of(member, question, member.getLevel());
    }

    @Transactional(readOnly = true)
    public PageResponse<FindQuestionResponse> findAllQuestions(Pageable pageable) {

        Integer currentPage = pageable.getPageNumber()+1;

        Page<Question> pages = questionRepository.findAll(pageable);

        Integer totalPages = pages.getTotalPages();

        if (totalPages == 0) totalPages+=1;

        if (currentPage > totalPages) {
            throw new BusinessException(QuestionErrorCode.NOT_FOUND_PAGE);
        }

        Pagination pagination = Pagination.builder()
            .totalPage(totalPages)
            .pageable(pages.getSize())
            .isEnd(currentPage.equals(totalPages))
            .build();

        List<FindQuestionResponse> responsePages = pages.getContent().stream()
            .map(Question::getId)
            .map(this::findQuestion)
            .toList();

        return PageResponse.of(pagination, responsePages);
    }

    @Transactional
    public void updateQuestion(Long questionId, PutQuestionRequest putQuestionRequest) {
        Question question = questionRepository.findById(questionId)
            .orElseThrow(() -> new BusinessException(QuestionErrorCode.NOT_FOUND_QUESTION));

        question.update(putQuestionRequest.title(), putQuestionRequest.content(), putQuestionRequest.imageUrl());

        List<String> skills = putQuestionRequest.skills();
        List<QuestionTechStack> techStackList = new ArrayList<>();

        questionTechStackRepository.deleteAllByQuestionId(questionId);

        saveTechStackList(question, skills, techStackList);
    }

    public void saveTechStackList(Question question, List<String> skills, List<QuestionTechStack> techStackList) {
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
        questionRepository.findById(questionId).orElseThrow(() -> new BusinessException(QuestionErrorCode.NOT_FOUND_QUESTION));

        questionRepository.deleteById(questionId);

        questionTechStackRepository.deleteAllByQuestionId(questionId);
    }
}

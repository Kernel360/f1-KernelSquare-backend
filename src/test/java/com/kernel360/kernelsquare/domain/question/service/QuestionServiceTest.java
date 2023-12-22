package com.kernel360.kernelsquare.domain.question.service;

import com.kernel360.kernelsquare.domain.level.entity.Level;
import com.kernel360.kernelsquare.domain.level.repository.LevelRepository;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.member.repository.MemberRepository;
import com.kernel360.kernelsquare.domain.question.dto.CreateQuestionRequest;
import com.kernel360.kernelsquare.domain.question.dto.FindQuestionResponse;
import com.kernel360.kernelsquare.domain.question.dto.UpdateQuestionRequest;
import com.kernel360.kernelsquare.domain.question.entity.Question;
import com.kernel360.kernelsquare.domain.question.repository.QuestionRepository;
import com.kernel360.kernelsquare.domain.tech_stack.entity.TechStack;
import com.kernel360.kernelsquare.domain.tech_stack.repository.TechStackRepository;
import com.kernel360.kernelsquare.global.common_response.error.code.QuestionErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;
import com.kernel360.kernelsquare.global.dto.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("질문 서비스 통합 테스트")
@Transactional
@SpringBootTest
class QuestionServiceTest {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TechStackRepository techStackRepository;
    @Autowired
    private LevelRepository levelRepository;

    Member testMember;

    Question testQuestion;

    Level testLevel;

    private Question createTestQuestion() {
        return Question.builder()
            .title("테스트")
            .content("성공하자")
            .imageUrl("test.jpg")
            .viewCount(0L)
            .closedStatus(false)
            .member(testMember)
            .techStackList(List.of())
            .build();
    }

    private Member createTestMember() {
        return Member
            .builder()
            .nickname("hongjugwang")
            .email("jugwang@naver.com")
            .password("hashedPassword")
            .experience(10000L)
            .introduction("hi, i'm hongjugwang.")
            .imageUrl("s3:qwe12fasdawczx")
            .level(testLevel)
            .build();
    }

    private TechStack createTestTechStack(String skill) {
        return TechStack.builder()
            .skill(skill)
            .build();
    }

    private Level createTestLevel() {
        return Level.builder()
            .name(6L)
            .imageUrl("1.jpg")
            .build();
    }

    @BeforeEach
    void setUp() {
        Level level = createTestLevel();
        testLevel = levelRepository.save(level);

        TechStack techStack = createTestTechStack("Java");
        techStackRepository.save(techStack);

        techStack = createTestTechStack("Python");
        techStackRepository.save(techStack);

        Member member = createTestMember();
        testMember = memberRepository.save(member);

        Question question = createTestQuestion();
        testQuestion = questionRepository.save(question);
    }

    @Test
    @DisplayName("질문 생성 테스트")
    void testCreateQuestion() {
        //given
        String testTitle = "java";
        String testContent = "what is java";
        String testImageUrl = "1.jpg";
        List<String> testSkills = List.of("Java", "Python");

        //when
        Long testQuestionId = questionService.createQuestion(new CreateQuestionRequest(
            testMember.getId(), testTitle, testContent, testImageUrl, testSkills
        ));

        Question testCreatedQuestion = questionRepository.findById(testQuestionId)
            .orElseThrow(() -> new BusinessException(QuestionErrorCode.QUESTION_NOT_FOUND));

        //then
        assertThat(testCreatedQuestion).isNotNull();
        assertThat(testCreatedQuestion.getTitle()).isEqualTo(testTitle);
        assertThat(testCreatedQuestion.getContent()).isEqualTo(testContent);
        assertThat(testCreatedQuestion.getImageUrl()).isEqualTo(testImageUrl);
        assertThat(testCreatedQuestion.getTechStackList().stream().map(x -> x.getTechStack().getSkill()).toList()).isEqualTo(testSkills);
        assertThat(testCreatedQuestion.getMember().getId()).isEqualTo(testMember.getId());
    }

    @Test
    @DisplayName("질문 조회 테스트")
    void testFindQuestion() {
        //given
        Long testQuestionId = testQuestion.getId();

        //when
        FindQuestionResponse testFindQuestionResponse = questionService.findQuestion(testQuestionId);

        //then
        assertThat(testFindQuestionResponse).isNotNull();
        assertThat(testFindQuestionResponse.id()).isEqualTo(testQuestion.getId());
        assertThat(testFindQuestionResponse.title()).isEqualTo(testQuestion.getTitle());
        assertThat(testFindQuestionResponse.content()).isEqualTo(testQuestion.getContent());
        assertThat(testFindQuestionResponse.questionImageUrl()).isEqualTo(testQuestion.getImageUrl());
        assertThat(testFindQuestionResponse.nickname()).isEqualTo(testMember.getNickname());
        assertThat(testFindQuestionResponse.memberImageUrl()).isEqualTo(testMember.getImageUrl());
        assertThat(testFindQuestionResponse.level()).isEqualTo(testMember.getLevel().getName());
        assertThat(testFindQuestionResponse.levelImageUrl()).isEqualTo(testMember.getImageUrl());
        assertThat(testFindQuestionResponse.skills()).isEqualTo(testQuestion.getTechStackList()
            .stream().map(x -> x.getTechStack().getSkill()).toList());
        //ToDo 답변에 대한 로직이 구현된 후 해당 질문에 대한 답변 list가 잘담기는지 테스트해야 하는지 생각해볼 필요가 있음
    }

    @Test
    @DisplayName("모든 질문 조회 테스트")
    void testFindAllQuestions() {
        //given
        Pageable testPageable = PageRequest.of(0, 3);

        Integer testCurrentPage = testPageable.getPageNumber() + 1;

        Page<Question> testPages = questionRepository.findAll(testPageable);

        Integer testTotalPages = testPages.getTotalPages();

        if (testTotalPages == 0) testTotalPages+=1;

        //when
        PageResponse<FindQuestionResponse> testPageResponse = questionService.findAllQuestions(testPageable);

        //then
        assertThat(testPageResponse).isNotNull();
        assertThat(testPageResponse.pagination().totalPage()).isEqualTo(testTotalPages);
        assertThat(testPageResponse.pagination().pageable()).isEqualTo(testPages.getSize());
        assertThat(testPageResponse.pagination().isEnd()).isEqualTo(testCurrentPage.equals(testTotalPages));
        assertThat(testPageResponse.list()).isNotNull();
    }

    @Test
    @DisplayName("질문 수정 테스트")
    void testUpdateQuestion() {
        //given
        String testTitle = "질문 수정 테스트";
        String testContent = "질문 수정 테스트1";
        String testImageUrl = "putTest.jpg";
        List<String> testSkills = List.of("Java");

        //when
        questionService.updateQuestion(testQuestion.getId(), new UpdateQuestionRequest(testTitle, testContent, testImageUrl, testSkills));

        //then
        assertThat(testQuestion).isNotNull();
        assertThat(testQuestion.getTitle()).isEqualTo(testTitle);
        assertThat(testQuestion.getContent()).isEqualTo(testContent);
        assertThat(testQuestion.getImageUrl()).isEqualTo(testImageUrl);
        assertThat(testQuestion.getTechStackList().stream().map(x -> x.getTechStack().getSkill()).toList()).isEqualTo(testSkills);
    }

    @Test
    @DisplayName("질문 삭제 테스트")
    void testDeleteQuestion() {
        //given
        Long TestQuestionId = testQuestion.getId();

        //when
        questionService.deleteQuestion(TestQuestionId);

        BusinessException TestException = assertThrows(BusinessException.class, () ->
            questionRepository.findById(TestQuestionId).orElseThrow(() ->
                new BusinessException(QuestionErrorCode.QUESTION_NOT_FOUND)));

        //then
        assertThat(TestException).isNotNull();
        assertThat(TestException.getErrorCode()).isExactlyInstanceOf(QuestionErrorCode.class);
        assertThat(TestException.getErrorCode()).isEqualTo(QuestionErrorCode.QUESTION_NOT_FOUND);
        assertThat(TestException.getErrorCode().getStatus()).isEqualTo(QuestionErrorCode.QUESTION_NOT_FOUND.getStatus());
        assertThat(TestException.getErrorCode().getMsg()).isEqualTo(QuestionErrorCode.QUESTION_NOT_FOUND.getMsg());
    }
}
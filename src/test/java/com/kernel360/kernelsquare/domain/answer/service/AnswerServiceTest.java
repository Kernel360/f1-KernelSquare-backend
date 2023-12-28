package com.kernel360.kernelsquare.domain.answer.service;

import com.kernel360.kernelsquare.domain.answer.dto.CreateAnswerRequest;
import com.kernel360.kernelsquare.domain.answer.dto.FindAnswerResponse;
import com.kernel360.kernelsquare.domain.answer.entity.Answer;
import com.kernel360.kernelsquare.domain.answer.repository.AnswerRepository;
import com.kernel360.kernelsquare.domain.member.dto.UpdateMemberRequest;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.member.repository.MemberRepository;
import com.kernel360.kernelsquare.domain.member.service.MemberService;
import com.kernel360.kernelsquare.domain.question.entity.Question;
import com.kernel360.kernelsquare.domain.question.repository.QuestionRepository;
import com.kernel360.kernelsquare.global.common_response.error.code.MemberErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DisplayName("답변 서비스 통합 테스트")
@Transactional
@SpringBootTest
@ActiveProfiles("test")
public class AnswerServiceTest {
    @Autowired
    private AnswerService answerService;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private QuestionRepository questionRepository;

    Long createdQuestionId;
    Question testQuestion;

    Long createdMemberId;
    Member testMember;

    CreateAnswerRequest createAnswerRequest;
    List<Answer> testAnswers = new ArrayList<>();

    @BeforeEach
    void setUp() {
        Question question = createTestQuestion();
        testQuestion = questionRepository.save(question);
        createdQuestionId = testQuestion.getId();

        for (int i=1; i<4; i++) {
            testMember = memberRepository.save(createTestMember(i));
            testAnswers.add(answerRepository.save(createTestAnswer((long) i, testMember, testQuestion)));
        }
        createdMemberId = testMember.getId();
    }

    @Test
    @DisplayName("질문에 대한 답변 조회")
    void findAllAnswer() throws Exception {
        //given
        Long newQuestionId = createdQuestionId;
        //when
        List<FindAnswerResponse> newAnswerList = answerService.findAllAnswer(newQuestionId);
        //then
        assertThat(testAnswers.size()).isEqualTo(newAnswerList.size());
    }

    @Test
    @DisplayName("질문에 대한 답변 생성")
    void createAnswer() throws Exception {
        //given
        createAnswerRequest = new CreateAnswerRequest(
                createdMemberId,
                createdQuestionId,
                "Test Content",
                "Test Image URL"
        );
        //when
        Long newCreatedAnswerId = answerService.createAnswer(createAnswerRequest);
        //then
        assertThat(answerRepository.findById(newCreatedAnswerId).isPresent()).isTrue();
        assertThat(createAnswerRequest.content()).isEqualTo(answerRepository.findById(newCreatedAnswerId).get().getContent());
    }


    private Question createTestQuestion() {
        return Question
                .builder()
                .title("Test Question")
                .content("Test Content")
                .imageUrl("S3:TestImage")
                .closedStatus(false)
                .build();
    }

    private Member createTestMember(int index) {
        return Member
                .builder()
                .nickname("hongjugwang" + index)
                .email("jugwang" + index + "@naver.com")
                .password("hashedPassword")
                .experience(10000L)
                .introduction("hi, i'm hongjugwang.")
                .imageUrl("s3:qwe12fasdawczx")
                .build();
    }

    private Answer createTestAnswer(Long index, Member member, Question question) {
        return Answer
                .builder()
                .content("Test Answer" + index)
                .voteCount(index)
                .imageUrl("S3:TestAnswer" + index)
                .member(member)
                .question(question)
                .build();
    }
}

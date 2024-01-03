package com.kernel360.kernelsquare.domain.question_tech_stack.repository;

import com.kernel360.kernelsquare.domain.level.entity.Level;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.question.entity.Question;
import com.kernel360.kernelsquare.domain.question_tech_stack.entity.QuestionTechStack;
import com.kernel360.kernelsquare.domain.tech_stack.entity.TechStack;
import com.kernel360.kernelsquare.global.config.JpaAuditingConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("질문 - 기술 스택 레포지토리 통합 테스트")
@DataJpaTest
@Import(JpaAuditingConfig.class)
class QuestionTechStackRepositoryTest {
    @Autowired
    private QuestionTechStackRepository questionTechStackRepository;

    Member member;
    Level level;

    private Question createTestQuestion(Long id) {
        return Question.builder()
            .id(id)
            .title("테스트")
            .content("성공하자")
            .imageUrl("test.jpg")
            .viewCount(0L)
            .closedStatus(false)
            .member(member)
            .techStackList(List.of())
            .build();
    }

    private Member createTestMember() {
        return Member.builder()
            .id(1L)
            .nickname("hongjugwang")
            .email("jugwang@naver.com")
            .password("hashedPassword")
            .experience(10000L)
            .introduction("hi, i'm hongjugwang.")
            .imageUrl("s3:qwe12fasdawczx")
            .level(level)
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
        level = createTestLevel();

        member = createTestMember();
    }

    @Test
    @DisplayName("질문 - 기술 스택 deleteAllByQuestionId 정상 작동 테스트")
    void testDeleteAllByQuestionId() {
        //given
        Question question = createTestQuestion(1L);

        TechStack techStack1 = new TechStack(1L, "TDD");
        TechStack techStack2 = new TechStack(2L, "jwt");

        QuestionTechStack questionTechStack1 = new QuestionTechStack(techStack1, question);
        QuestionTechStack questionTechStack2 = new QuestionTechStack(techStack2, question);

        questionTechStackRepository.saveAll(List.of(questionTechStack1, questionTechStack2));

        //when
        questionTechStackRepository.deleteAllByQuestionId(question.getId());

        //then
        List<QuestionTechStack> questionTechStacks = questionTechStackRepository.findAll();
        assertThat(questionTechStacks).isEmpty();
    }
}
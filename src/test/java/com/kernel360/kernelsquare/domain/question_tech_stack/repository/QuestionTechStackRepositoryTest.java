package com.kernel360.kernelsquare.domain.question_tech_stack.repository;

import com.kernel360.kernelsquare.global.config.JpaAuditingConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("질문 - 기술 스택 레포지토리 통합 테스트")
@DataJpaTest
@Import(JpaAuditingConfig.class)
class QuestionTechStackRepositoryTest {
    @Autowired
    private QuestionTechStackRepository questionTechStackRepository;

    @Test
    @DisplayName("질문 - 기술 스택 deleteAllByQuestionId 정상 작동 테스트")
    void testDeleteAllByQuestionId() {
        //given

    }
}
package com.kernel360.kernelsquare.domain.tech_stack.repository;

import com.kernel360.kernelsquare.domain.tech_stack.entity.TechStack;
import com.kernel360.kernelsquare.global.common_response.error.code.TechStackErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;
import com.kernel360.kernelsquare.global.config.JpaAuditingConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("기술 스택 레포지토리 통합 테스트")
@DataJpaTest
@Import(JpaAuditingConfig.class)
class TechStackRepositoryTest {
    @Autowired
    private TechStackRepository techStackRepository;

    @Test
    @DisplayName("기술 스택 findBySkill 정상 작동 테스트")
    void testFindBySkill() {
        //given
        TechStack techStack = new TechStack(1L, "Java");

        TechStack saveTechStack = techStackRepository.save(techStack);

        //when
        TechStack findTechStack = techStackRepository.findBySkill(techStack.getSkill())
            .orElseThrow(() -> new BusinessException(TechStackErrorCode.TECH_STACK_NOT_FOUND));

        //then
        assertThat(findTechStack).isNotNull();
        assertThat(findTechStack).isEqualTo(saveTechStack);
    }

    @Test
    @DisplayName("기술 스택 existsBySkill 정상 작동 테스트")
    void testExistsBySkill() {
        //given
        TechStack techStack = new TechStack(1L, "Python");

        techStackRepository.save(techStack);

        //when
        boolean isSkillExists = techStackRepository.existsBySkill(techStack.getSkill());

        //then
        assertThat(isSkillExists).isTrue();
    }
}
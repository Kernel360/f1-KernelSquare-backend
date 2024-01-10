package com.kernel360.kernelsquare.domain.search.repository;

import com.kernel360.kernelsquare.domain.question.entity.Question;
import com.kernel360.kernelsquare.global.config.QueryDslConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("검색 레포지토리 통합 테스트")
@DataJpaTest
@Import({QueryDslConfig.class, CustomQuestionRepositoryImpl.class})
class CustomQuestionRepositoryTest {
    @Autowired
    private CustomQuestionRepository customQuestionRepository;

    @Test
    @DisplayName("검색 searchByKeyword 정상 작동 테스트")
    void testSearchByKeyword() {
        //given
        Pageable pageable = PageRequest.of(0, 5);

        String keyword = "Java";

        //when
        Page<Question> page = customQuestionRepository.searchByKeyword(pageable, keyword);

        //then
        assertThat(page).isNotNull();
        assertThat(page.getContent()).isNotNull();
    }
}
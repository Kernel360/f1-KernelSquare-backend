package com.kernelsquare.domainmysql.domain.search.repository;

import static org.assertj.core.api.Assertions.*;

import com.kernelsquare.domainmysql.domain.tech_stack.entity.TechStack;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.kernelsquare.domainmysql.config.DBConfig;
import com.kernelsquare.domainmysql.domain.question.entity.Question;

@DisplayName("검색 레포지토리 단위 테스트")
@DataJpaTest
@Import({DBConfig.class, SearchRepositoryImpl.class})
class SearchRepositoryTest {
	@Autowired
	private SearchRepository searchRepository;

	@Test
	@DisplayName("검색 searchQuestionsByKeyword 정상 작동 테스트")
	void testSearchQuestionsByKeyword() {
		//given
		Pageable pageable = PageRequest.of(0, 5);

		String keyword = "Java";

		//when
		Page<Question> page = searchRepository.searchQuestionsByKeyword(pageable, keyword);

		//then
		assertThat(page).isNotNull();
		assertThat(page.getContent()).isNotNull();
	}

	@Test
	@DisplayName("검색 searchTechStacksByKeyword 정상 작동 테스트")
	void testSearchTechStacksByKeyword() {
		//given
		Pageable pageable = PageRequest.of(0, 5);

		String keyword = "j";

		//when
		Page<TechStack> page = searchRepository.searchTechStacksByKeyword(pageable, keyword);

		//then
		assertThat(page).isNotNull();
		assertThat(page.getContent()).isNotNull();
	}
}
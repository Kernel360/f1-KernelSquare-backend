package com.kernelsquare.memberapi.domain.tech_stack.service;

import com.kernelsquare.core.dto.PageResponse;
import com.kernelsquare.domainmysql.domain.tech_stack.entity.TechStack;
import com.kernelsquare.domainmysql.domain.tech_stack.repository.TechStackRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@DisplayName("기술 스택 서비스 단위 테스트")
@ExtendWith(MockitoExtension.class)
class TechStackServiceTest {
	@InjectMocks
	private TechStackService techStackService;
	@Mock
	private TechStackRepository techStackRepository;

	@Test
	@DisplayName("기술 스택 모든 조회 테스트")
	void testFindAllTechStacks() {
		//given
		List<TechStack> techStackList = Arrays.asList(
			new TechStack(1L, "JavaScript"),
			new TechStack(2L, "Python")
		);

		Pageable pageable = PageRequest.of(0, 2);
		Page<TechStack> pages = new PageImpl<>(techStackList, pageable, techStackList.size());

		given(techStackRepository.findAll(any(PageRequest.class))).willReturn(pages);

		Integer currentPage = pageable.getPageNumber() + 1;

		Integer totalPages = pages.getTotalPages();

		if (totalPages == 0)
			totalPages += 1;

		//when
		PageResponse<String> pageResponse = techStackService.findAllTechStacks(pageable);

		//then
		assertThat(pageResponse).isNotNull();
		assertThat(pageResponse.pagination().totalPage()).isEqualTo(totalPages);
		assertThat(pageResponse.pagination().pageable()).isEqualTo(pages.getSize());
		assertThat(pageResponse.pagination().isEnd()).isEqualTo(currentPage.equals(totalPages));
		assertThat(pageResponse.list()).isNotNull();

		//verify
		verify(techStackRepository, times(1)).findAll(any(PageRequest.class));
	}
}
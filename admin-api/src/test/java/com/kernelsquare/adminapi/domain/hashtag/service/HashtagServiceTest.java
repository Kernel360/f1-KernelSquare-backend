package com.kernelsquare.adminapi.domain.hashtag.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kernelsquare.adminapi.domain.hashtag.dto.FindAllHashtagResponse;
import com.kernelsquare.domainmysql.domain.hashtag.entity.Hashtag;
import com.kernelsquare.domainmysql.domain.hashtag.repository.HashtagRepository;

@DisplayName("해시태그 서비스 단위 테스트")
@ExtendWith(MockitoExtension.class)
class HashtagServiceTest {
	@InjectMocks
	private HashtagService hashtagService;
	@Mock
	private HashtagRepository hashtagRepository;

	@Test
	@DisplayName("모든 해시태그 조회 테스트")
	void testFindAllHashtag() {
		// Given
		List<Hashtag> expectedHashtags = Arrays.asList(
			Hashtag.builder().content("#김밥천국").build(),
			Hashtag.builder().content("#라멘").build()
		);
		given(hashtagRepository.findAll()).willReturn(expectedHashtags);

		// When
		FindAllHashtagResponse actualHashtags = hashtagService.findAllHashtag();

		// Then
		assertThat(actualHashtags.hashtags().get(0).hashtagId()).isEqualTo(expectedHashtags.get(0).getId());
		assertThat(actualHashtags.hashtags().get(1).hashtagId()).isEqualTo(expectedHashtags.get(1).getId());
		assertThat(actualHashtags.hashtags().get(0).content()).isEqualTo(expectedHashtags.get(0).getContent());
		assertThat(actualHashtags.hashtags().get(1).content()).isEqualTo(expectedHashtags.get(1).getContent());

		verify(hashtagRepository, times(1)).findAll();
	}

	@Test
	@DisplayName("해시태그 삭제 테스트")
	void testDeleteHashtag() {
		// Given
		Hashtag hashtag = Hashtag.builder().id(1L).content("#김밥천국").build();

		doNothing().when(hashtagRepository).deleteById(hashtag.getId());

		// When
		hashtagService.deleteHashtag(hashtag.getId());

		// Then
		verify(hashtagRepository).deleteById(hashtag.getId());
	}

}
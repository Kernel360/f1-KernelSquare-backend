package com.kernelsquare.memberapi.domain.hashtag.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kernelsquare.domainmysql.domain.hashtag.entity.Hashtag;
import com.kernelsquare.domainmysql.domain.hashtag.repository.HashtagRepository;
import com.kernelsquare.memberapi.domain.hashtag.dto.FindAllHashtagResponse;
import com.kernelsquare.memberapi.domain.hashtag.dto.FindHashtagResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HashtagService {
	private final HashtagRepository hashtagRepository;

	@Transactional(readOnly = true)
	public FindAllHashtagResponse findAllHashtag() {

		List<FindHashtagResponse> result = new ArrayList<>();

		List<Hashtag> hashtagList = hashtagRepository.findAll();
		for (Hashtag hashtag : hashtagList) {
			result.add(FindHashtagResponse.from(hashtag));
		}
		return FindAllHashtagResponse.from(result);
	}

	@Transactional
	public void deleteHashtag(Long hashtagId) {
		hashtagRepository.deleteById(hashtagId);
	}
}

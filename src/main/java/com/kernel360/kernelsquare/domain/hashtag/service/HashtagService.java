package com.kernel360.kernelsquare.domain.hashtag.service;

import com.kernel360.kernelsquare.domain.hashtag.dto.FindAllHashtagResponse;
import com.kernel360.kernelsquare.domain.hashtag.entity.Hashtag;
import com.kernel360.kernelsquare.domain.hashtag.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HashtagService {

    private final HashtagRepository hashtagRepository;

    @Transactional(readOnly = true)
    public FindAllHashtagResponse findAllHashtag() {
        List<Hashtag> hashtagList = hashtagRepository.findAll();
        return FindAllHashtagResponse.from(hashtagList);
    }

    @Transactional
    public void deleteHashtag(Long hashtagId) { hashtagRepository.deleteById(hashtagId); }
}

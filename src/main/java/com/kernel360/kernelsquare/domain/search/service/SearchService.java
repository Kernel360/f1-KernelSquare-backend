package com.kernel360.kernelsquare.domain.search.service;

import com.kernel360.kernelsquare.domain.search.entity.SearchItem;
import com.kernel360.kernelsquare.domain.search.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final SearchRepository searchRepository;

    public List<SearchItem> search(String query) {
        return searchRepository.findByTitleContainingOrContentContainingOrTechStackContaining(query, query, query);
    }
}

package com.kernel360.kernelsquare.domain.search.repository;

import com.kernel360.kernelsquare.domain.search.entity.SearchItem;
import org.springframework.stereotype.Repository;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

@Repository
public interface SearchRepository extends ElasticsearchRepository<SearchItem, String> {
    List<SearchItem> findByTitleContainingOrContentContainingOrTechStackContaining(String title, String content, String techStack);
}

package com.kernel360.kernelsquare.domain.search.entity;

import jakarta.persistence.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "search")
public record SearchItem(
    @Id
    String id,
//    String type,
    String title,
    String content,
    String techStack
) {
}

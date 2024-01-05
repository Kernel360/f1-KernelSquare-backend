package com.kernel360.kernelsquare.domain.search.controller;

import com.kernel360.kernelsquare.domain.search.entity.SearchItem;
import com.kernel360.kernelsquare.domain.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping("/search")
    public ResponseEntity<List<SearchItem>> searchQuestions(@RequestParam String query) {
        List<SearchItem> searchResults = searchService.search(query);
        return ResponseEntity.ok(searchResults);
    }
}

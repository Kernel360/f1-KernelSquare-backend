    package com.kernel360.kernelsquare.domain.search.controller;

    import com.kernel360.kernelsquare.domain.question.dto.FindQuestionResponse;
    import com.kernel360.kernelsquare.domain.search.service.SearchService;
    import com.kernel360.kernelsquare.global.common_response.ApiResponse;
    import com.kernel360.kernelsquare.global.common_response.ResponseEntityFactory;
    import com.kernel360.kernelsquare.global.dto.PageResponse;
    import lombok.RequiredArgsConstructor;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.web.PageableDefault;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestParam;
    import org.springframework.web.bind.annotation.RestController;

    import static com.kernel360.kernelsquare.global.common_response.response.code.SearchResponseCode.SEARCH_QUESTION_COMPLETED;

    @RestController
    @RequestMapping("/api/v1")
    @RequiredArgsConstructor
    public class SearchController {
        private final SearchService searchService;

        @GetMapping("/search/questions")
        public ResponseEntity<ApiResponse<PageResponse<FindQuestionResponse>>> searchQuestions(
            @PageableDefault(page = 0, size = 5)
            Pageable pageable,
            @RequestParam
            String keyword
        ) {
            PageResponse<FindQuestionResponse> searchResults = searchService.searchQuestions(pageable, keyword);
            return ResponseEntityFactory.toResponseEntity(SEARCH_QUESTION_COMPLETED, searchResults);
        }
    }

package com.kernel360.kernelsquare.domain.question.controller;

import com.kernel360.kernelsquare.domain.question.dto.CreateQuestionRequest;
import com.kernel360.kernelsquare.domain.question.dto.FindQuestionResponse;
import com.kernel360.kernelsquare.domain.question.dto.PutQuestionRequest;
import com.kernel360.kernelsquare.domain.question.service.QuestionService;
import com.kernel360.kernelsquare.global.common_response.ApiResponse;
import com.kernel360.kernelsquare.global.common_response.ResponseEntityFactory;
import com.kernel360.kernelsquare.global.dto.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.kernel360.kernelsquare.global.common_response.response.code.QuestionResponseCode.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping("/questions")
    public ResponseEntity<ApiResponse> createQuestion(
        @Valid
        @RequestBody
        CreateQuestionRequest createQuestionRequest
    ) {
        questionService.createQuestion(createQuestionRequest);

        return ResponseEntityFactory.toResponseEntity(QUESTION_CREATED);
    }

    @GetMapping("/questions/{questionId}")
    public ResponseEntity<ApiResponse<FindQuestionResponse>> findQuestion(
        @PathVariable
        Long questionId
    ) {
        return ResponseEntityFactory.toResponseEntity(QUESTION_FOUND, questionService.findQuestion(questionId));
    }

    @GetMapping("questions")
    public ResponseEntity<ApiResponse<PageResponse<FindQuestionResponse>>> findAllQuestions(
        @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.DESC)
        Pageable pageable
    ) {
        return ResponseEntityFactory.toResponseEntity(QUESTION_ALL_FOUND, questionService.findAllQuestions(pageable));
    }

    @PutMapping("/questions/{questionId}")
    public ResponseEntity<ApiResponse> updateQuestion(
        @PathVariable
        Long questionId,
        @Valid
        @RequestBody
        PutQuestionRequest putQuestionRequest
    ) {
        questionService.updateQuestion(questionId, putQuestionRequest);

        return ResponseEntityFactory.toResponseEntity(QUESTION_UPDATED);
    }

    @DeleteMapping("/questions/{questionId}")
    public ResponseEntity<ApiResponse> deleteQuestion(
        @PathVariable
        Long questionId
    ) {
        questionService.deleteQuestion(questionId);

        return ResponseEntityFactory.toResponseEntity(QUESTION_DELETED);
    }
}

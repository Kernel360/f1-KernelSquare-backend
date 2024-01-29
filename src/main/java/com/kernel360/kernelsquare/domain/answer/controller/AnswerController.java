package com.kernel360.kernelsquare.domain.answer.controller;

import com.kernel360.kernelsquare.domain.answer.dto.CreateAnswerRequest;
import com.kernel360.kernelsquare.domain.answer.dto.FindAllAnswerResponse;
import com.kernel360.kernelsquare.domain.answer.dto.FindAnswerResponse;
import com.kernel360.kernelsquare.domain.answer.dto.UpdateAnswerRequest;
import com.kernel360.kernelsquare.domain.answer.service.AnswerService;
import com.kernel360.kernelsquare.global.common_response.ApiResponse;
import com.kernel360.kernelsquare.global.common_response.ResponseEntityFactory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.kernel360.kernelsquare.global.common_response.response.code.AnswerResponseCode.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @GetMapping("/questions/{questionId}/answers")
    public ResponseEntity<ApiResponse<FindAllAnswerResponse>> findAllAnswers(@PathVariable Long questionId) {
        FindAllAnswerResponse findAllAnswerResponse = answerService.findAllAnswer(questionId);
        return ResponseEntityFactory.toResponseEntity(ANSWERS_ALL_FOUND, findAllAnswerResponse);
    }

    @PostMapping("/questions/{questionId}/answers")
    public ResponseEntity<ApiResponse> createAnswer(
            @Valid @RequestBody CreateAnswerRequest createAnswerRequest,
            @PathVariable Long questionId
    ) {
        answerService.createAnswer(createAnswerRequest, questionId);
        return ResponseEntityFactory.toResponseEntity(ANSWER_CREATED);
    }

    @PutMapping("/questions/answers/{answerId}")
    public ResponseEntity<ApiResponse> updateAnswer(
            @Valid @RequestBody UpdateAnswerRequest updateAnswerRequest,
            @PathVariable Long answerId
    ) {
        answerService.updateAnswer(updateAnswerRequest, answerId);
        return ResponseEntityFactory.toResponseEntity(ANSWER_UPDATED);
    }

    @DeleteMapping("/questions/answers/{answerId}")
    public ResponseEntity<ApiResponse> deleteAnswer(
            @PathVariable Long answerId
    ) {
        answerService.deleteAnswer(answerId);
        return ResponseEntityFactory.toResponseEntity(ANSWER_DELETED);
    }
}


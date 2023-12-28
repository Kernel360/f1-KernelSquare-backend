package com.kernel360.kernelsquare.domain.answer.controller;

import com.kernel360.kernelsquare.domain.answer.dto.CreateAnswerRequest;
import com.kernel360.kernelsquare.domain.answer.dto.FindAnswerResponse;
import com.kernel360.kernelsquare.domain.answer.service.AnswerService;
import com.kernel360.kernelsquare.global.common_response.ApiResponse;
import com.kernel360.kernelsquare.global.common_response.ResponseEntityFactory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.kernel360.kernelsquare.global.common_response.response.code.AnswerResponseCode.ANSWERS_ALL_FOUND;
import static com.kernel360.kernelsquare.global.common_response.response.code.AnswerResponseCode.ANSWER_CREATED;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @GetMapping("/questions/{questionId}/answers")
    public ResponseEntity<ApiResponse<List<FindAnswerResponse>>> findAllAnswers(@PathVariable Long questionId) {
        List<FindAnswerResponse> findAnswerResponses = answerService.findAllAnswer(questionId);
        return ResponseEntityFactory.toResponseEntity(ANSWERS_ALL_FOUND, findAnswerResponses);
    }

    @PostMapping("/questions/{questionId}/answers")
    public ResponseEntity<ApiResponse<Long>> createAnswer(
            @Valid @RequestBody CreateAnswerRequest createAnswerRequest,
            @PathVariable Long questionId
    ) {
        Long answerId = answerService.createAnswer(createAnswerRequest, questionId);
        return ResponseEntityFactory.toResponseEntity(ANSWER_CREATED, answerId);
    }
}


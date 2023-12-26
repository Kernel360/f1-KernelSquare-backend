package com.kernel360.kernelsquare.domain.answer.controller;

import com.kernel360.kernelsquare.domain.answer.dto.FindAnswerResponse;
import com.kernel360.kernelsquare.domain.answer.service.AnswerService;
import com.kernel360.kernelsquare.global.common_response.ApiResponse;
import com.kernel360.kernelsquare.global.common_response.ResponseEntityFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.kernel360.kernelsquare.global.common_response.response.code.AnswerResponseCode.ANSWERS_ALL_FOUND;

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
}

package com.kernel360.kernelsquare.domain.answer.controller;

import com.kernel360.kernelsquare.domain.answer.dto.FindAnswerResponse;
import com.kernel360.kernelsquare.domain.answer.entity.Answer;
import com.kernel360.kernelsquare.domain.answer.service.AnswerService;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.question.entity.Question;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.kernel360.kernelsquare.global.common_response.response.code.AnswerResponseCode.ANSWERS_ALL_FOUND;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@DisplayName("답변 컨트롤러 단위 테스트")
@WebMvcTest(AnswerController.class)
public class AnswerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AnswerService answerService;
    private final Long testQuestionId = 1L;
    private final Question testQuestion = Question
            .builder()
            .title("Test Question")
            .content("Test Content")
            .imageUrl("S3:TestImage")
            .closedStatus(false)
            .build();

    private final Member testMember = Member
            .builder()
            .nickname("hongjugwang")
            .email("jugwang@naver.com")
            .password("hashedPassword")
            .experience(10000L)
            .introduction("hi, i'm hongjugwang.")
            .imageUrl("s3:qwe12fasdawczx")
            .build();

    private final Answer testAnswer = Answer
            .builder()
            .content("Test Answer Content")
            .voteCount(10L)
            .imageUrl("s3:AnswerImageURL")
            .member(testMember)
            .question(testQuestion)
            .build();

    private final FindAnswerResponse findAnswerResponse = FindAnswerResponse
            .builder()
            .id(1L)
            .questionId(1L)
            .content(testAnswer.getContent())
            .rankImageUrl("s3:RankURL")
            .createdBy("HongJuGwang")
            .answerImageUrl(testAnswer.getImageUrl())
            .memberImageUrl(testMember.getImageUrl())
            .createdDate(LocalDate.now().toString())
            .voteCount(testAnswer.getVoteCount())
            .build();

    private List<FindAnswerResponse> answerResponseList = new ArrayList<>();

    @Test
    @WithMockUser
    @DisplayName("답변 조회 성공시, 200 OK, 메시지, 답변정보를 반환한다.")
    void testFindAllAnswers() throws Exception {
        //given
        answerResponseList.add(findAnswerResponse);
        doReturn(answerResponseList)
                .when(answerService)
                .findAllAnswer(anyLong());

        //when & then
        mockMvc.perform(get("/api/v1/questions/" + testQuestionId + "/answers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().is(ANSWERS_ALL_FOUND.getStatus().value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ANSWERS_ALL_FOUND.getCode()))
                .andExpect(jsonPath("$.msg").value(ANSWERS_ALL_FOUND.getMsg()))
                .andExpect(jsonPath("$.data[0].content").value(testAnswer.getContent()))
                .andExpect(jsonPath("$.data[0].answer_image_url").value(testAnswer.getImageUrl()))
                .andExpect(jsonPath("$.data[0].vote_count").value(testAnswer.getVoteCount()));

        //verify
        verify(answerService, times(1)).findAllAnswer(anyLong());
    }
}

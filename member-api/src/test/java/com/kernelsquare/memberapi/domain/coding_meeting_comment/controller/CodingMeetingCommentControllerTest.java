package com.kernelsquare.memberapi.domain.coding_meeting_comment.controller;

import com.kernelsquare.memberapi.domain.coding_meeting_comment.dto.CodingMeetingCommentDto;
import com.kernelsquare.memberapi.domain.coding_meeting_comment.service.CodingMeetingCommentFacade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.kernelsquare.core.common_response.response.code.CodingMeetingCommentResponseCode.CODING_MEETING_COMMENT_ALL_FOUND;
import static com.kernelsquare.memberapi.config.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("모각코 댓글 컨트롤러 테스트")
@WebMvcTest(CodingMeetingCommentController.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
public class CodingMeetingCommentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CodingMeetingCommentFacade codingMeetingCommentFacade;

    @Test
    @WithMockUser
    @DisplayName("전체 모각코 댓글 조회 성공시 200 OK와 성공 응답을 반환한다.")
    void testFindAllCodingMeetingComment() throws Exception {
        String testCodingMeetingToken = "cm_IhXXI0H70OY9vHqOp";
        CodingMeetingCommentDto.FindAllResponse findAllResponse = CodingMeetingCommentDto.FindAllResponse.builder()
                .memberId(1L)
                .memberNickname("커널스")
                .memberProfileUrl("/member/ejqofijwefji3")
                .memberLevelImageUrl("/level/level2")
                .memberLevel(2L)
                .createdDate(LocalDateTime.of(2023, 3, 2, 12, 0, 0))
                .codingMeetingCommentToken("cmc_uriewfhi2h49")
                .codingMeetingCommentContent("참여할까말까요?")
                .build();

        List<CodingMeetingCommentDto.FindAllResponse> response = new ArrayList<>(List.of(findAllResponse));

        doReturn(response).when(codingMeetingCommentFacade).findAllCodingMeetingComment(anyString());

        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/coding-meeting-comments/{codingMeetingToken}", testCodingMeetingToken)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        resultActions
                .andExpect(status().is(CODING_MEETING_COMMENT_ALL_FOUND.getStatus().value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(
                        document("coding-meetings", getDocumentResponse(), pathParameters(
                        parameterWithName("codingMeetingToken").description("모각코 토큰")),
                        responseFields(fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("응답"),
                                fieldWithPath("data[0].member_id").type(JsonFieldType.NUMBER).description("회원 아이디"),
                                fieldWithPath("data[0].member_level").type(JsonFieldType.NUMBER).description("회원 레벨"),
                                fieldWithPath("data[0].member_nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
                                fieldWithPath("data[0].member_profile_url").type(JsonFieldType.STRING).description("회원 프로필 이미지"),
                                fieldWithPath("data[0].member_level_image_url").type(JsonFieldType.STRING).description("회원 레벨 이미지"),
                                fieldWithPath("data[0].created_date").type(JsonFieldType.STRING).description("모각코 댓글 생성일시"),
                                fieldWithPath("data[0].coding_meeting_comment_token").type(JsonFieldType.STRING).description("모각코 댓글 토큰"),
                                fieldWithPath("data[0].coding_meeting_comment_content").type(JsonFieldType.STRING).description("모각코 댓글 내용")
                        )
                ));
    }
}

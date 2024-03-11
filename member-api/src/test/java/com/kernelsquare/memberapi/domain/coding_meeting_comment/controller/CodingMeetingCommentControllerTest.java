package com.kernelsquare.memberapi.domain.coding_meeting_comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdapter;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdaptorInstance;
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

import static com.kernelsquare.core.common_response.response.code.CodingMeetingCommentResponseCode.*;
import static com.kernelsquare.memberapi.config.ApiDocumentUtils.getDocumentRequest;
import static com.kernelsquare.memberapi.config.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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
    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
            .setPropertyNamingStrategy(PropertyNamingStrategies.SnakeCaseStrategy.INSTANCE);

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
                                responseFields(
                                        fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
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

        verify(codingMeetingCommentFacade, times(1)).findAllCodingMeetingComment(anyString());
    }

    @Test
    @WithMockUser
    @DisplayName("모각코 댓글 생성 성공시 200 OK와 성공 응답을 반환한다.")
    void testCreateCodingMeetingComment() throws Exception {
        Member member = Member.builder()
                .id(1L)
                .email("test@email.com")
                .nickname("커널스")
                .build();

        MemberAdapter memberAdapter = new MemberAdapter(MemberAdaptorInstance.of(member));

        CodingMeetingCommentDto.CreateRequest createRequest = CodingMeetingCommentDto.CreateRequest.builder()
                .codingMeetingToken("cm_ZZZZZZZZZZZ")
                .codingMeetingCommentContent("모각코 참여할게요 오오오")
                .build();

        doNothing().when(codingMeetingCommentFacade).createCodingMeetingComment(any(CodingMeetingCommentDto.CreateRequest.class), anyLong());

        String jsonRequest = objectMapper.writeValueAsString(createRequest);

        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/coding-meeting-comments")
                .with(csrf())
                .with(user(memberAdapter))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonRequest)
        );

        resultActions
                .andExpect(status().is(CODING_MEETING_COMMENT_CREATED.getStatus().value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(
                        document("coding-meeting-comments", getDocumentRequest(), getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("coding_meeting_token").type(JsonFieldType.STRING).description("모각코 토큰"),
                                        fieldWithPath("coding_meeting_comment_content").type(JsonFieldType.STRING).description("모각코 댓글 내용")
                                ),
                                responseFields(
                                        fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드")
                                )
                        ));

        verify(codingMeetingCommentFacade, times(1)).createCodingMeetingComment(any(CodingMeetingCommentDto.CreateRequest.class), anyLong());
    }

    @Test
    @WithMockUser
    @DisplayName("모각코 댓글 수정 성공시 200 OK와 성공 응답을 반환한다.")
    void testUpdateCodingMeetingComment() throws Exception {
        String testCodingMeetingCommentToken = "cmc_ZZZZZZZZZZZZ";
        Member member = Member.builder()
                .id(1L)
                .email("test@email.com")
                .nickname("커널스")
                .build();

        MemberAdapter memberAdapter = new MemberAdapter(MemberAdaptorInstance.of(member));

        CodingMeetingCommentDto.UpdateRequest updateRequest = CodingMeetingCommentDto.UpdateRequest.builder()
                .codingMeetingCommentContent("모각코 참여할게요 오오오 수정")
                .build();

        doNothing().when(codingMeetingCommentFacade).updateCodingMeetingComment(any(CodingMeetingCommentDto.UpdateRequest.class), anyString());

        String jsonRequest = objectMapper.writeValueAsString(updateRequest);

        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.put("/api/v1/coding-meeting-comments/{codingMeetingCommentToken}", testCodingMeetingCommentToken)
                .with(csrf())
                .with(user(memberAdapter))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonRequest)
        );

        resultActions
                .andExpect(status().is(CODING_MEETING_COMMENT_UPDATED.getStatus().value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(
                        document("coding-meeting-comments", getDocumentRequest(), getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("coding_meeting_comment_content").type(JsonFieldType.STRING).description("모각코 댓글 수정 내용")
                                ),
                                responseFields(
                                        fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드")
                                )
                        ));

        verify(codingMeetingCommentFacade, times(1)).updateCodingMeetingComment(any(CodingMeetingCommentDto.UpdateRequest.class), anyString());
    }

    @Test
    @WithMockUser
    @DisplayName("모각코 댓글 삭제 성공시 200 OK와 성공 응답을 반환한다.")
    void testDeleteCodingMeetingComment() throws Exception {
        String testCodingMeetingCommentToken = "cmc_ZZZZZZZZZZZZ";
        Member member = Member.builder()
                .id(1L)
                .email("test@email.com")
                .nickname("커널스")
                .build();

        MemberAdapter memberAdapter = new MemberAdapter(MemberAdaptorInstance.of(member));

        doNothing().when(codingMeetingCommentFacade).deleteCodingMeetingComment(anyString());

        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/v1/coding-meeting-comments/{codingMeetingCommentToken}", testCodingMeetingCommentToken)
                .with(csrf())
                .with(user(memberAdapter))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        resultActions
                .andExpect(status().is(CODING_MEETING_COMMENT_DELETED.getStatus().value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(
                        document("coding-meeting-comments", getDocumentResponse(),
                                responseFields(
                                        fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드")
                                )
                        ));

        verify(codingMeetingCommentFacade, times(1)).deleteCodingMeetingComment(anyString());
    }

}

package com.kernelsquare.memberapi.domain.coding_meeting.controller;

import com.kernelsquare.memberapi.domain.coding_meeting.dto.CodingMeetingDto;
import com.kernelsquare.memberapi.domain.coding_meeting.service.CodingMeetingFacade;
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
import java.util.List;

import static com.kernelsquare.core.common_response.response.code.CodingMeetingResponseCode.CODING_MEETING_FOUND;
import static com.kernelsquare.memberapi.config.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("모각코 컨트롤러 테스트")
@WebMvcTest(CodingMeetingController.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
public class CodingMeetingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CodingMeetingFacade codingMeetingFacade;

    @Test
    @WithMockUser
    @DisplayName("단일 모각코 조회 성공시 200 OK와 성공 응답을 반환한다.")
    void testFindCodingMeeting() throws Exception {
        String testCodingMeetingToken = "cm_IhXXI0H70OY9vHqOp";
        CodingMeetingDto.FindResponse response = CodingMeetingDto.FindResponse.builder()
                .memberId(1L)
                .memberLevel(2L)
                .memberNickname("커널스")
                .memberProfileUrl("/member/ejqofijwefji3")
                .memberLevelImageUrl("/level/level2")
                .codingMeetingTitle("테스트 제목")
                .codingMeetingToken(testCodingMeetingToken)
                .codingMeetingContent("테스트 내용")
                .codingMeetingHashtags(List.of("해시태그1", "해시태그2"))
                .codingMeetingLocationId("34952784")
                .codingMeetingLocationPlaceName("카페카페카페카페")
                .codingMeetingLocationLongitude("42.78953743")
                .codingMeetingLocationLatitude("129.7893523")
                .codingMeetingMemberUpperLimit(4L)
                .codingMeetingStartTime(LocalDateTime.of(2023, 3, 2, 12, 0, 0))
                .codingMeetingEndTime(LocalDateTime.of(2023, 3, 2, 16, 0, 0))
                .codingMeetingClosed(Boolean.FALSE)
                .build();


        doReturn(response).when(codingMeetingFacade).findCodingMeeting(anyString());

        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/coding-meetings/{codingMeetingToken}", testCodingMeetingToken)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        resultActions
                .andExpect(status().is(CODING_MEETING_FOUND.getStatus().value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(
                        document("coding-meetings", getDocumentResponse(), pathParameters(
                        parameterWithName("codingMeetingToken").description("모각코 토큰")),
                        responseFields(fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답"),
                                fieldWithPath("data.member_id").type(JsonFieldType.NUMBER).description("회원 아이디"),
                                fieldWithPath("data.member_level").type(JsonFieldType.NUMBER).description("회원 레벨"),
                                fieldWithPath("data.member_nickname").type(JsonFieldType.STRING).description("회원 닉네임"),
                                fieldWithPath("data.member_profile_url").type(JsonFieldType.STRING).description("회원 프로필 이미지"),
                                fieldWithPath("data.member_level_image_url").type(JsonFieldType.STRING).description("회원 레벨 이미지"),
                                fieldWithPath("data.coding_meeting_title").type(JsonFieldType.STRING).description("모각코 제목"),
                                fieldWithPath("data.coding_meeting_token").type(JsonFieldType.STRING).description("모각코 토큰"),
                                fieldWithPath("data.coding_meeting_hashtags").type(JsonFieldType.ARRAY).description("모각코 해시태그"),
                                fieldWithPath("data.coding_meeting_location_id").type(JsonFieldType.STRING).description("모각코 위치 아이디"),
                                fieldWithPath("data.coding_meeting_location_place_name").type(JsonFieldType.STRING).description("모각코 위치명"),
                                fieldWithPath("data.coding_meeting_location_longitude").type(JsonFieldType.STRING).description("모각코 경도"),
                                fieldWithPath("data.coding_meeting_location_latitude").type(JsonFieldType.STRING).description("모각코 위도"),
                                fieldWithPath("data.coding_meeting_member_upper_limit").type(JsonFieldType.NUMBER).description("모각코 회원 상한"),
                                fieldWithPath("data.coding_meeting_start_time").type(JsonFieldType.STRING).description("모각코 시작시간"),
                                fieldWithPath("data.coding_meeting_end_time").type(JsonFieldType.STRING).description("모각코 종료시간"),
                                fieldWithPath("data.coding_meeting_content").type(JsonFieldType.STRING).description("모각코 내용"),
                                fieldWithPath("data.coding_meeting_closed").type(JsonFieldType.BOOLEAN).description("모각코 마감 여부")
                        )
                ));
    }
}

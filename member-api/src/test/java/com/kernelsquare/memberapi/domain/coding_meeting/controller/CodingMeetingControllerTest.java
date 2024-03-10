package com.kernelsquare.memberapi.domain.coding_meeting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kernelsquare.core.dto.PageResponse;
import com.kernelsquare.domainmysql.domain.coding_meeting.entity.CodingMeeting;
import com.kernelsquare.domainmysql.domain.coding_meeting.info.CodingMeetingInfo;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdapter;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdaptorInstance;
import com.kernelsquare.memberapi.domain.coding_meeting.dto.CodingMeetingDto;
import com.kernelsquare.memberapi.domain.coding_meeting.service.CodingMeetingFacade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.kernelsquare.core.common_response.response.code.CodingMeetingResponseCode.*;
import static com.kernelsquare.memberapi.config.ApiDocumentUtils.getDocumentRequest;
import static com.kernelsquare.memberapi.config.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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
    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
            .setPropertyNamingStrategy(PropertyNamingStrategies.SnakeCaseStrategy.INSTANCE);

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
                                parameterWithName("codingMeetingToken").description("모각코 토큰")), responseFields(
                                        fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
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

        verify(codingMeetingFacade, times(1)).findCodingMeeting(anyString());
    }

    @Test
    @WithMockUser
    @DisplayName("전체 모각코 조회 성공시 200 OK와 성공 응답을 반환한다.")
    void testFindAllCodingMeeting() throws Exception {

        String testCodingMeetingTokenPrefix = "cm_zzzzzzzzz";

        Level level = Level.builder()
                .id(1L)
                .name(1L)
                .imageUrl("levelURL")
                .levelUpperLimit(300L)
                .build();

        Member member = Member.builder()
                .id(1L)
                .email("test@email.com")
                .nickname("커널스")
                .level(level)
                .build();

        MemberAdapter memberAdapter = new MemberAdapter(MemberAdaptorInstance.of(member));

        CodingMeeting codingMeeting = CodingMeeting.builder()
                .codingMeetingTitle("테스트 제목")
                .codingMeetingContent("테스트 내용")
                .codingMeetingStartTime(LocalDateTime.of(2024, 1, 30, 12, 10, 30))
                .codingMeetingEndTime(LocalDateTime.of(2024, 1, 30, 18, 10, 30))
                .codingMeetingMemberUpperLimit(3L)
                .member(member)
                .build();

        CodingMeetingInfo.ListInfo codingMeetingListInfo = CodingMeetingInfo.ListInfo.of(codingMeeting);

        List<CodingMeetingInfo.ListInfo> codingMeetingList = new ArrayList<>(List.of(codingMeetingListInfo));

        Page<CodingMeetingInfo.ListInfo> page = new PageImpl<>(codingMeetingList);

        CodingMeetingDto.FindAllResponse response = CodingMeetingDto.FindAllResponse.builder()
                .memberId(1L)
                .memberLevel(2L)
                .memberNickname("커널스")
                .memberProfileUrl("/member/ejqofijwefji3")
                .memberLevelImageUrl("/level/level2")
                .codingMeetingTitle("테스트 제목")
                .codingMeetingToken(testCodingMeetingTokenPrefix + "01")
                .codingMeetingHashtags(List.of("해시태그1", "해시태그2"))
                .codingMeetingStartTime(LocalDateTime.of(2023, 3, 2, 12, 0, 0))
                .codingMeetingEndTime(LocalDateTime.of(2023, 3, 2, 16, 0, 0))
                .codingMeetingClosed(Boolean.FALSE)
                .createdDate(LocalDateTime.of(2023,2,28,13,25,10))
                .build();

        List<CodingMeetingDto.FindAllResponse> findAllResponseList = new ArrayList<>(List.of(response));

        Pageable pageable = Pageable.ofSize(1);

        PageResponse pageResponse = PageResponse.of(pageable, page, findAllResponseList);

        doReturn(pageResponse).when(codingMeetingFacade).findAllCodingMeeting(any(Pageable.class), anyString(), any(MemberAdapter.class));

        String jsonRequest = objectMapper.writeValueAsString(pageable);

        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/coding-meetings")
                .with(csrf())
                .with(user(memberAdapter))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonRequest)
        );

        resultActions
                .andExpect(status().is(CODING_MEETING_ALL_FOUND.getStatus().value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(
                        document("coding-meetings", getDocumentRequest(), getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("page_number").type(JsonFieldType.NUMBER).description("페이지"),
                                        fieldWithPath("page_size").type(JsonFieldType.NUMBER).description("페이지 수"),
                                        fieldWithPath("sort.empty").type(JsonFieldType.BOOLEAN).description("빈 페이지"),
                                        fieldWithPath("sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬 여부"),
                                        fieldWithPath("sort.unsorted").type(JsonFieldType.BOOLEAN).description("비정렬 여부"),
                                        fieldWithPath("offset").type(JsonFieldType.NUMBER).description("오프셋"),
                                        fieldWithPath("paged").type(JsonFieldType.BOOLEAN).description("페이징 여부"),
                                        fieldWithPath("unpaged").type(JsonFieldType.BOOLEAN).description("비페이징 여부")),
                                responseFields(
                                        fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                                        fieldWithPath("data.pagination.total_page").type(JsonFieldType.NUMBER).description("총 페이지 수"),
                                        fieldWithPath("data.pagination.pageable").type(JsonFieldType.NUMBER).description("페이지 당 항목 수"),
                                        fieldWithPath("data.pagination.is_end").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
                                        fieldWithPath("data.list[0].member_id").type(JsonFieldType.NUMBER).description("모각코 작성자 ID"),
                                        fieldWithPath("data.list[0].member_level").type(JsonFieldType.NUMBER).description("모각코 작성자 레벨"),
                                        fieldWithPath("data.list[0].member_nickname").type(JsonFieldType.STRING).description("모각코 작성자 닉네임"),
                                        fieldWithPath("data.list[0].member_profile_url").type(JsonFieldType.STRING).description("모각코 작성자 프로필 URL"),
                                        fieldWithPath("data.list[0].member_level_image_url").type(JsonFieldType.STRING).description("모각코 작성자 레벨 이미지 URL"),
                                        fieldWithPath("data.list[0].coding_meeting_title").type(JsonFieldType.STRING).description("모각코 제목"),
                                        fieldWithPath("data.list[0].coding_meeting_token").type(JsonFieldType.STRING).description("모각코 토큰"),
                                        fieldWithPath("data.list[0].coding_meeting_hashtags").type(JsonFieldType.ARRAY).description("모각코 "),
                                        fieldWithPath("data.list[0].coding_meeting_start_time").type(JsonFieldType.STRING).description("모각코 작성자 ID"),
                                        fieldWithPath("data.list[0].coding_meeting_end_time").type(JsonFieldType.STRING).description("모각코 작성자 ID"),
                                        fieldWithPath("data.list[0].coding_meeting_closed").type(JsonFieldType.BOOLEAN).description("모각코 작성자 ID"),
                                        fieldWithPath("data.list[0].created_date").type(JsonFieldType.STRING).description("모각코 작성자 ID")
                                )
                        ));

        verify(codingMeetingFacade, times(1)).findAllCodingMeeting(any(Pageable.class), anyString(), any(MemberAdapter.class));
    }

    @Test
    @WithMockUser
    @DisplayName("모각코 생성 성공시 200 OK와 성공 응답을 반환한다.")
    void testCreateCodingMeeting() throws Exception {
        Member member = Member.builder()
                .id(1L)
                .email("test@email.com")
                .nickname("커널스")
                .build();

        MemberAdapter memberAdapter = new MemberAdapter(MemberAdaptorInstance.of(member));

        CodingMeetingDto.CreateRequest createRequest = CodingMeetingDto.CreateRequest.builder()
                .codingMeetingTitle("삼성역 근처 모각코하고 봉은사 공양간에서 점심드실 분")
                .codingMeetingLocationId("1234512345")
                .codingMeetingLocationPlaceName("스타벅스 삼성점")
                .codingMeetingLocationLongitude("24.1234415")
                .codingMeetingLocationLatitude("123.12321421")
                .codingMeetingMemberUpperLimit(3L)
                .codingMeetingStartTime(LocalDateTime.of(2024, 2, 12, 12, 10, 10))
                .codingMeetingEndTime(LocalDateTime.of(2024, 2, 12, 18, 10, 10))
                .codingMeetingHashtags(new ArrayList<>(List.of("해시태그1", "해시태그2")))
                .codingMeetingContent("일반 텍스트로 작성된 모각코 소개글")
                .build();

        CodingMeetingDto.CreateResponse createResponse = CodingMeetingDto.CreateResponse.builder()
                .codingMeetingToken("cm_BHIFBWJIQD1234")
                .build();

        doReturn(createResponse).when(codingMeetingFacade).createCodingMeeting(any(CodingMeetingDto.CreateRequest.class), anyLong());

        String jsonRequest = objectMapper.writeValueAsString(createRequest);

        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/coding-meetings")
                .with(csrf())
                .with(user(memberAdapter))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonRequest)
        );

        resultActions
                .andExpect(status().is(CODING_MEETING_CREATED.getStatus().value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(
                        document("coding-meetings", getDocumentRequest(), getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("coding_meeting_title").type(JsonFieldType.STRING).description("모각코 제목"),
                                        fieldWithPath("coding_meeting_location_id").type(JsonFieldType.STRING).description("모각코 장소 ID"),
                                        fieldWithPath("coding_meeting_location_place_name").type(JsonFieldType.STRING).description("모각코 장소명"),
                                        fieldWithPath("coding_meeting_location_longitude").type(JsonFieldType.STRING).description("모각코 장소 경도"),
                                        fieldWithPath("coding_meeting_location_latitude").type(JsonFieldType.STRING).description("모각코 장소 위도"),
                                        fieldWithPath("coding_meeting_member_upper_limit").type(JsonFieldType.NUMBER).description("모각코 모집인원 상한"),
                                        fieldWithPath("coding_meeting_start_time").type(JsonFieldType.ARRAY).description("모각코 시작 일시"),
                                        fieldWithPath("coding_meeting_end_time").type(JsonFieldType.ARRAY).description("모각코 종료 일시"),
                                        fieldWithPath("coding_meeting_hashtags").type(JsonFieldType.ARRAY).description("모각코 해시태그 리스트"),
                                        fieldWithPath("coding_meeting_content").type(JsonFieldType.STRING).description("모각코 소개글")
                                ),
                                responseFields(
                                        fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                                        fieldWithPath("data.coding_meeting_token").type(JsonFieldType.STRING).description("생성된 모각코 고유 토큰")                                )
                        ));

        verify(codingMeetingFacade, times(1)).createCodingMeeting(any(CodingMeetingDto.CreateRequest.class), anyLong());
    }

    @Test
    @WithMockUser
    @DisplayName("모각코 수정 성공시 200 OK와 성공 응답을 반환한다.")
    void testUpdateCodingMeeting() throws Exception {
        String codingMeetingToken = "cm_zzzzzzzzz";

        CodingMeetingDto.UpdateRequest updateRequest = CodingMeetingDto.UpdateRequest.builder()
                .codingMeetingTitle("삼성역 근처 모각코하고 봉은사 공양간에서 점심드실 분")
                .codingMeetingLocationId("1234512345")
                .codingMeetingLocationPlaceName("스타벅스 삼성점일줄알았지?")
                .codingMeetingLocationLongitude("24.1234415")
                .codingMeetingLocationLatitude("123.12321421")
                .codingMeetingMemberUpperLimit(3L)
                .codingMeetingStartTime(LocalDateTime.of(2024, 2, 12, 12, 10, 10))
                .codingMeetingEndTime(LocalDateTime.of(2024, 2, 12, 18, 10, 10))
                .codingMeetingHashtags(new ArrayList<>(List.of("해시태그1수정", "해시태그2수정")))
                .codingMeetingContent("일반 텍스트로 작성된 모각코 소개글 수정")
                .build();

        doNothing().when(codingMeetingFacade).updateCodingMeeting(any(CodingMeetingDto.UpdateRequest.class), anyString());

        String jsonRequest = objectMapper.writeValueAsString(updateRequest);

        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.put("/api/v1/coding-meetings/{codingMeetingToken}", codingMeetingToken)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonRequest)
        );

        resultActions
                .andExpect(status().is(CODING_MEETING_UPDATED.getStatus().value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(
                        document("coding-meetings", getDocumentRequest(), getDocumentResponse(),
                                requestFields(
                                        fieldWithPath("coding_meeting_title").type(JsonFieldType.STRING).description("모각코 제목"),
                                        fieldWithPath("coding_meeting_location_id").type(JsonFieldType.STRING).description("모각코 장소 ID"),
                                        fieldWithPath("coding_meeting_location_place_name").type(JsonFieldType.STRING).description("모각코 장소명"),
                                        fieldWithPath("coding_meeting_location_longitude").type(JsonFieldType.STRING).description("모각코 장소 경도"),
                                        fieldWithPath("coding_meeting_location_latitude").type(JsonFieldType.STRING).description("모각코 장소 위도"),
                                        fieldWithPath("coding_meeting_member_upper_limit").type(JsonFieldType.NUMBER).description("모각코 모집인원 상한"),
                                        fieldWithPath("coding_meeting_start_time").type(JsonFieldType.ARRAY).description("모각코 시작 일시"),
                                        fieldWithPath("coding_meeting_end_time").type(JsonFieldType.ARRAY).description("모각코 종료 일시"),
                                        fieldWithPath("coding_meeting_hashtags").type(JsonFieldType.ARRAY).description("모각코 해시태그 리스트"),
                                        fieldWithPath("coding_meeting_content").type(JsonFieldType.STRING).description("모각코 소개글")
                                ),
                                responseFields(
                                        fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드")
                        ))
                );

        verify(codingMeetingFacade, times(1)).updateCodingMeeting(any(CodingMeetingDto.UpdateRequest.class), anyString());
    }

    @Test
    @WithMockUser
    @DisplayName("모각코 삭제 성공시 200 OK와 성공 응답을 반환한다.")
    void testDeleteCodingMeeting() throws Exception {
        String codingMeetingToken = "cm_zzzzzzzzz";

        doNothing().when(codingMeetingFacade).deleteCodingMeeting(anyString());

        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/v1/coding-meetings/{codingMeetingToken}", codingMeetingToken)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        resultActions
                .andExpect(status().is(CODING_MEETING_DELETED.getStatus().value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(
                        document("coding-meetings", getDocumentResponse(),
                                responseFields(
                                        fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드")
                                ))
                );

        verify(codingMeetingFacade, times(1)).deleteCodingMeeting(anyString());
    }

    @Test
    @WithMockUser
    @DisplayName("모각코 마감 성공시 200 OK와 성공 응답을 반환한다.")
    void testCloseCodingMeeting() throws Exception {
        String codingMeetingToken = "cm_zzzzzzzzz";

        doNothing().when(codingMeetingFacade).closeCodingMeeting(anyString());

        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.put("/api/v1/coding-meetings/{codingMeetingToken}/status", codingMeetingToken)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        resultActions
                .andExpect(status().is(CODING_MEETING_CLOSED.getStatus().value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(
                        document("coding-meetings", getDocumentResponse(),
                                responseFields(
                                        fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드")
                                ))
                );

        verify(codingMeetingFacade, times(1)).closeCodingMeeting(anyString());
    }
}

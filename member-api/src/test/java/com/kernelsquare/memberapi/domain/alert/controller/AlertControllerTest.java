package com.kernelsquare.memberapi.domain.alert.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kernelsquare.core.type.AuthorityType;
import com.kernelsquare.domainmongodb.domain.alert.entity.Alert;
import com.kernelsquare.domainmysql.domain.authority.entity.Authority;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member_authority.entity.MemberAuthority;
import com.kernelsquare.memberapi.domain.alert.dto.AlertDto;
import com.kernelsquare.memberapi.domain.alert.facade.AlertFacade;
import com.kernelsquare.memberapi.domain.alert.manager.SseManager;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdapter;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdaptorInstance;
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
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.List;

import static com.kernelsquare.core.common_response.response.code.AlertResponseCode.MY_ALERT_ALL_FOUND;
import static com.kernelsquare.memberapi.config.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Alert 컨트롤러 단위 테스트")
@WebMvcTest(AlertController.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
class AlertControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SseManager sseManager;
    @MockBean
    private AlertFacade alertFacade;

    @Test
    @DisplayName("SSE 구독 성공 시 200 OK를 반환한다.")
    @WithMockUser
    void testSubscribe() throws Exception {
        Member member = Member.builder()
            .id(1L)
            .nickname("machine")
            .email("awdag@nsavasc.om")
            .password("hashed")
            .experience(1200L)
            .introduction("basfas")
            .authorities(List.of(
                MemberAuthority.builder()
                    .member(Member.builder().build())
                    .authority(Authority.builder().authorityType(AuthorityType.ROLE_USER).build())
                    .build()))
            .imageUrl("agawsc")
            .build();

        MemberAdapter memberAdapter = new MemberAdapter(MemberAdaptorInstance.of(member));

        SseEmitter sseEmitter = new SseEmitter(60 * 1000L);

        doReturn(sseEmitter).when(sseManager).subscribe(memberAdapter);

        //when
        ResultActions resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/v1/alerts/sse")
                .with(user(memberAdapter))
                .contentType(MediaType.TEXT_EVENT_STREAM_VALUE)
                .accept(MediaType.TEXT_EVENT_STREAM_VALUE)
                .characterEncoding("UTF-8"));

        //then
        resultActions.andExpect(status().isOk())
            //TODO 응답 contentType이 null이 맞나?
            .andDo(document("alert-subscribe"));
    }

    @Test
    @DisplayName("알림 조회 성공시 200 OK와 정상 응답을 반환한다.")
    @WithMockUser
    void testFindAllAlerts() throws Exception {
        //given
        Member member = Member.builder()
            .id(1L)
            .nickname("machine")
            .email("awdag@nsavasc.om")
            .password("hashed")
            .experience(1200L)
            .introduction("basfas")
            .authorities(List.of(
                MemberAuthority.builder()
                    .member(Member.builder().build())
                    .authority(Authority.builder().authorityType(AuthorityType.ROLE_USER).build())
                    .build()))
            .imageUrl("agawsc")
            .build();

        MemberAdapter memberAdapter = new MemberAdapter(MemberAdaptorInstance.of(member));

        AlertDto.FindAllResponse findAllResponse = AlertDto.FindAllResponse.builder()
            .recipientId("1")
            .recipient("시나모롤")
            .senderId("2")
            .sender("강형욱")
            .message("므헷헷헷x999999")
            .alertType(Alert.AlertType.QUESTION_REPLY)
            .sendTime(LocalDateTime.now())
            .build();

        List<AlertDto.FindAllResponse> findAllResponseList = List.of(findAllResponse);

        AlertDto.PersonalFindAllResponse response = AlertDto.PersonalFindAllResponse.from(findAllResponseList);

        doReturn(response).when(alertFacade).findAllAlerts(eq(memberAdapter));

        //when
        ResultActions resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/v1/alerts")
                .with(user(memberAdapter))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"));

        //then
        resultActions.andExpect(status().is(MY_ALERT_ALL_FOUND.getStatus().value()))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andDo(document("my-alert-all-found", getDocumentResponse(),
                responseFields(
                    fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                    fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
                    fieldWithPath("data.personal_alert_list[].recipient_id").type(JsonFieldType.STRING).description("알림 수신자 ID"),
                    fieldWithPath("data.personal_alert_list[].recipient").type(JsonFieldType.STRING).description("알림 수신자 닉네임"),
                    fieldWithPath("data.personal_alert_list[].sender_id").type(JsonFieldType.STRING).description("알림 송신자 ID"),
                    fieldWithPath("data.personal_alert_list[].sender").type(JsonFieldType.STRING).description("알림 송신자 닉네임"),
                    fieldWithPath("data.personal_alert_list[].message").type(JsonFieldType.STRING).description("알림 메시지"),
                    fieldWithPath("data.personal_alert_list[].alert_type").type(JsonFieldType.STRING).description("알림 타입"),
                    fieldWithPath("data.personal_alert_list[].send_time").type(JsonFieldType.STRING).description("알림 보낸 시간")
                )));
    }
}
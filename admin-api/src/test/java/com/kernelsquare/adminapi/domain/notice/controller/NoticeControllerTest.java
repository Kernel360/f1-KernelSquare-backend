package com.kernelsquare.adminapi.domain.notice.controller;

import static com.kernelsquare.adminapi.config.ApiDocumentUtils.*;
import static com.kernelsquare.core.common_response.response.code.NoticeResponseCode.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kernelsquare.adminapi.domain.notice.dto.NoticeDto;
import com.kernelsquare.adminapi.domain.notice.service.NoticeFacade;
import com.kernelsquare.core.dto.PageResponse;
import com.kernelsquare.domainmysql.domain.notice.entity.Notice;
import com.kernelsquare.domainmysql.domain.notice.info.NoticeInfo;

@DisplayName("공지 컨트롤러 테스트")
@WebMvcTest(NoticeController.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
public class NoticeControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private NoticeFacade noticeFacade;
	private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
		.setPropertyNamingStrategy(PropertyNamingStrategies.SnakeCaseStrategy.INSTANCE);

	@Test
	@DisplayName("공지 생성 성공 시 200 OK와 성공 응답을 반환한다.")
	@WithMockUser
	void testCreateNotice() throws Exception {
		//given
		NoticeDto.CreateRequest request = NoticeDto.CreateRequest.builder()
			.noticeCategory("일반 공지")
			.noticeContent("Lets roll out")
			.noticeTitle("환불 관련 공지입니다.")
			.build();

		NoticeDto.FindResponse response = NoticeDto.FindResponse.builder()
			.noticeTitle("환불 관련 공지입니다.")
			.noticeToken("ntc_dawdawdwa")
			.noticeContent("Lets roll out")
			.noticeCategory(Notice.NoticeCategory.GENERAL)
			.createdDate(LocalDateTime.now())
			.modifiedDate(LocalDateTime.now())
			.build();

		String jsonRequest = objectMapper.writeValueAsString(request);

		doReturn(response).when(noticeFacade).createNotice(any(NoticeDto.CreateRequest.class));

		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/notices")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8")
			.content(jsonRequest));

		//then
		resultActions.andExpect(status().is(NOTICE_CREATED.getStatus().value()))
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andDo(document("notice-created", getDocumentRequest(), getDocumentResponse(),
				requestFields(fieldWithPath("notice_title").type(JsonFieldType.STRING).description("공지 제목"),
					fieldWithPath("notice_content").type(JsonFieldType.STRING).description("공지 내용"),
					fieldWithPath("notice_category").type(JsonFieldType.STRING).description("공지 카테고리")),
				responseFields(fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
					fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답"),
					fieldWithPath("data.notice_title").type(JsonFieldType.STRING).description("공지 제목"),
					fieldWithPath("data.notice_content").type(JsonFieldType.STRING).description("공지 내용"),
					fieldWithPath("data.notice_token").type(JsonFieldType.STRING).description("공지 토큰"),
					fieldWithPath("data.notice_category").type(JsonFieldType.STRING).description("일반 공지"),
					fieldWithPath("data.created_date").type(JsonFieldType.STRING).description("생성 시간"),
					fieldWithPath("data.modified_date").type(JsonFieldType.STRING).description("수정 시간"))));

		//verify
		verify(noticeFacade, times(1)).createNotice(any(NoticeDto.CreateRequest.class));
	}

	@Test
	@WithMockUser
	@DisplayName("단일 공지 조회 성공시 200 OK와 성공 응답을 반환한다.")
	void testFindNotice() throws Exception {
		//given
		String noticeToken = "ntc_d21awdawegaef";

		NoticeDto.FindResponse response = NoticeDto.FindResponse.builder()
			.noticeTitle("환불 관련 공지입니다.")
			.noticeToken("ntc_dawdawdwa")
			.noticeContent("Lets roll out")
			.noticeCategory(Notice.NoticeCategory.GENERAL)
			.createdDate(LocalDateTime.now())
			.modifiedDate(LocalDateTime.now())
			.build();

		doReturn(response).when(noticeFacade).findNotice(anyString());

		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/notices/" + noticeToken)
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8"));

		//then
		resultActions.andExpect(status().is(NOTICE_FOUND.getStatus().value()))
			.andDo(document("notice-found", getDocumentResponse(),
				responseFields(fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
					fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답"),
					fieldWithPath("data.notice_title").type(JsonFieldType.STRING).description("공지 제목"),
					fieldWithPath("data.notice_content").type(JsonFieldType.STRING).description("공지 내용"),
					fieldWithPath("data.notice_token").type(JsonFieldType.STRING).description("공지 토큰"),
					fieldWithPath("data.notice_category").type(JsonFieldType.STRING).description("일반 공지"),
					fieldWithPath("data.created_date").type(JsonFieldType.STRING).description("생성 시간"),
					fieldWithPath("data.modified_date").type(JsonFieldType.STRING).description("수정 시간"))));

		//verify
		verify(noticeFacade, times(1)).findNotice(anyString());
	}

	@Test
	@WithMockUser
	@DisplayName("공지 수정 성공 시 200 OK와 성공 응답을 반환한다.")
	void testUpdateNotice() throws Exception {
		NoticeDto.UpdateRequest request = NoticeDto.UpdateRequest.builder()
			.noticeToken("ntc_dwadwada")
			.noticeContent("공지를 수정해보죠ㅁㅈㄹㅁㅈㄹ")
			.noticeTitle("공지를 수정해보")
			.build();

		NoticeDto.FindResponse response = NoticeDto.FindResponse.builder()
			.noticeTitle("환불 관련 공지입니다.")
			.noticeToken("ntc_dawdawdwa")
			.noticeContent("Lets roll out")
			.noticeCategory(Notice.NoticeCategory.GENERAL)
			.createdDate(LocalDateTime.now())
			.modifiedDate(LocalDateTime.now())
			.build();

		String jsonRequest = objectMapper.writeValueAsString(request);

		doReturn(response).when(noticeFacade).updateNotice(any(NoticeDto.UpdateRequest.class));

		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.put("/api/v1/notices")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8")
			.content(jsonRequest));

		//then
		resultActions.andExpect(status().is(NOTICE_UPDATED.getStatus().value()))
			.andDo(document("notice-updated", getDocumentRequest(), getDocumentResponse(),
				requestFields(fieldWithPath("notice_token").type(JsonFieldType.STRING).description("공지 토큰"),
					fieldWithPath("notice_title").type(JsonFieldType.STRING).description("공지 제목"),
					fieldWithPath("notice_content").type(JsonFieldType.STRING).description("공지 내용")),
				responseFields(fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
					fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답"),
					fieldWithPath("data.notice_title").type(JsonFieldType.STRING).description("공지 제목"),
					fieldWithPath("data.notice_content").type(JsonFieldType.STRING).description("공지 내용"),
					fieldWithPath("data.notice_token").type(JsonFieldType.STRING).description("공지 토큰"),
					fieldWithPath("data.notice_category").type(JsonFieldType.STRING).description("일반 공지"),
					fieldWithPath("data.created_date").type(JsonFieldType.STRING).description("생성 시간"),
					fieldWithPath("data.modified_date").type(JsonFieldType.STRING).description("수정 시간"))));

		//verify
		verify(noticeFacade, times(1)).updateNotice(any(NoticeDto.UpdateRequest.class));
	}

	@Test
	@WithMockUser
	@DisplayName("공지 삭제 성공 시 200 OK와 성공 응답을 반환한다.")
	void testDeleteNotice() throws Exception {
		NoticeDto.DeleteRequest request = NoticeDto.DeleteRequest.builder().noticeToken("ntc_dkwkdowjddowaawd").build();

		String jsonRequest = objectMapper.writeValueAsString(request);

		doNothing().when(noticeFacade).deleteNotice(any(NoticeDto.DeleteRequest.class));

		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/v1/notices")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8")
			.content(jsonRequest));

		//then
		resultActions.andExpect(status().is(NOTICE_DELETED.getStatus().value()))
			.andDo(document("notice-deleted", getDocumentRequest(), getDocumentResponse(),
				requestFields(fieldWithPath("notice_token").type(JsonFieldType.STRING).description("공지 토큰")),
				responseFields(fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드"))));

		//verify
		verify(noticeFacade, times(1)).deleteNotice(any(NoticeDto.DeleteRequest.class));
	}

	@Test
	@WithMockUser
	@DisplayName("모든 공지 조회 성공 시 200 OK와 성공 응답을 반환한다.")
	void testFindAllNotice() throws Exception {

		Notice notice = Notice.builder()
			.noticeTitle("글이사라졌나요?")
			.noticeContent("글이 사라졌을까요?????")
			.noticeCategory(Notice.NoticeCategory.GENERAL)
			.build();

		NoticeInfo noticeInfo = NoticeInfo.of(notice);

		List<NoticeInfo> noticeInfoList = new ArrayList<>(List.of(noticeInfo));

		Page<NoticeInfo> page = new PageImpl<>(noticeInfoList);

		NoticeDto.FindAllResponse findAllResponse = NoticeDto.FindAllResponse.builder()
			.noticeTitle("글이사라졌나요?")
			.noticeCategory(Notice.NoticeCategory.GENERAL)
			.noticeToken("ntc_12gaegaaega")
			.createdDate(LocalDateTime.now())
			.modifiedDate(LocalDateTime.now())
			.build();

		List<NoticeDto.FindAllResponse> findAllResponseList = new ArrayList<>(List.of(findAllResponse));

		Pageable pageable = Pageable.ofSize(1);

		PageResponse pageResponse = PageResponse.of(pageable, page, findAllResponseList);

		doReturn(pageResponse).when(noticeFacade).findAllNotice(any(Pageable.class));

		String jsonRequest = objectMapper.writeValueAsString(pageable);

		//when
		ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/notices")
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.characterEncoding("UTF-8")
			.content(jsonRequest));

		//then
		resultActions.andExpect(status().is(NOTICE_ALL_FOUND.getStatus().value()))
			.andDo(document("notice-all-found", getDocumentRequest(), getDocumentResponse(),
				requestFields(fieldWithPath("page_number").type(JsonFieldType.NUMBER).description("페이지"),
					fieldWithPath("page_size").type(JsonFieldType.NUMBER).description("페이지 수"),
					fieldWithPath("sort.empty").type(JsonFieldType.BOOLEAN).description("빈 페이지"),
					fieldWithPath("sort.sorted").type(JsonFieldType.BOOLEAN).description("정렬 여부"),
					fieldWithPath("sort.unsorted").type(JsonFieldType.BOOLEAN).description("비정렬 여부"),
					fieldWithPath("offset").type(JsonFieldType.NUMBER).description("오프셋"),
					fieldWithPath("paged").type(JsonFieldType.BOOLEAN).description("페이징 여부"),
					fieldWithPath("unpaged").type(JsonFieldType.BOOLEAN).description("비페이징 여부")),
				responseFields(fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
					fieldWithPath("data.pagination.total_page").type(JsonFieldType.NUMBER).description("총 페이지 수"),
					fieldWithPath("data.pagination.pageable").type(JsonFieldType.NUMBER).description("pageable"),
					fieldWithPath("data.pagination.is_end").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"),
					fieldWithPath("data.list[0].notice_title").type(JsonFieldType.STRING).description("공지 제목"),
					fieldWithPath("data.list[0].notice_token").type(JsonFieldType.STRING).description("공지 토큰"),
					fieldWithPath("data.list[0].notice_category").type(JsonFieldType.STRING).description("공지 카테고리"),
					fieldWithPath("data.list[0].created_date").type(JsonFieldType.STRING).description("공지 생성일"),
					fieldWithPath("data.list[0].modified_date").type(JsonFieldType.STRING).description("공지 수정일"))));

		//verify
		verify(noticeFacade, times(1)).findAllNotice(any(Pageable.class));
	}

	@Test
	@WithMockUser
	@DisplayName("공지 카테고리를 일반으로 변경 성공 시 200 OK와 성공 응답을 반환한다.")
	void testChangeGeneral() throws Exception {
		NoticeDto.UpdateCategoryRequest request = NoticeDto.UpdateCategoryRequest.builder()
			.noticeToken("ntc_adwag22gsdv")
			.build();

		NoticeDto.FindResponse response = NoticeDto.FindResponse.builder()
			.noticeTitle("환불 관련 공지입니다.")
			.noticeToken("ntc_dawdawdwa")
			.noticeContent("Lets roll out")
			.noticeCategory(Notice.NoticeCategory.GENERAL)
			.createdDate(LocalDateTime.now())
			.modifiedDate(LocalDateTime.now())
			.build();

		String jsonRequest = objectMapper.writeValueAsString(request);

		doReturn(response).when(noticeFacade).changeGeneral(any(NoticeDto.UpdateCategoryRequest.class));

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.put("/api/v1/notices/change-general")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest));

		//then
		resultActions.andExpect(status().is(NOTICE_UPDATED.getStatus().value()))
			.andDo(document("notice-change-general", getDocumentRequest(), getDocumentResponse(),
				requestFields(fieldWithPath("notice_token").type(JsonFieldType.STRING).description("공지 토큰")),
				responseFields(fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
					fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답"),
					fieldWithPath("data.notice_title").type(JsonFieldType.STRING).description("공지 제목"),
					fieldWithPath("data.notice_content").type(JsonFieldType.STRING).description("공지 내용"),
					fieldWithPath("data.notice_token").type(JsonFieldType.STRING).description("공지 토큰"),
					fieldWithPath("data.notice_category").type(JsonFieldType.STRING).description("일반 공지"),
					fieldWithPath("data.created_date").type(JsonFieldType.STRING).description("생성 시간"),
					fieldWithPath("data.modified_date").type(JsonFieldType.STRING).description("수정 시간"))));

		//verify
		verify(noticeFacade, times(1)).changeGeneral(any(NoticeDto.UpdateCategoryRequest.class));
	}

	@Test
	@WithMockUser
	@DisplayName("공지 카테고리를 커피챗으로 변경 성공 시 200 OK와 성공 응답을 반환한다.")
	void testChangeCoffeeChat() throws Exception {
		NoticeDto.UpdateCategoryRequest request = NoticeDto.UpdateCategoryRequest.builder()
			.noticeToken("ntc_adwag22gsdv")
			.build();

		NoticeDto.FindResponse response = NoticeDto.FindResponse.builder()
			.noticeTitle("환불 관련 공지입니다.")
			.noticeToken("ntc_dawdawdwa")
			.noticeContent("Lets roll out")
			.noticeCategory(Notice.NoticeCategory.COFFEE_CHAT)
			.createdDate(LocalDateTime.now())
			.modifiedDate(LocalDateTime.now())
			.build();

		String jsonRequest = objectMapper.writeValueAsString(request);

		doReturn(response).when(noticeFacade).changeCoffeeChat(any(NoticeDto.UpdateCategoryRequest.class));

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.put("/api/v1/notices/change-coffee-chat")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest));

		//then
		resultActions.andExpect(status().is(NOTICE_UPDATED.getStatus().value()))
			.andDo(document("notice-change-coffee-chat", getDocumentRequest(), getDocumentResponse(),
				requestFields(fieldWithPath("notice_token").type(JsonFieldType.STRING).description("공지 토큰")),
				responseFields(fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
					fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답"),
					fieldWithPath("data.notice_title").type(JsonFieldType.STRING).description("공지 제목"),
					fieldWithPath("data.notice_content").type(JsonFieldType.STRING).description("공지 내용"),
					fieldWithPath("data.notice_token").type(JsonFieldType.STRING).description("공지 토큰"),
					fieldWithPath("data.notice_category").type(JsonFieldType.STRING).description("일반 공지"),
					fieldWithPath("data.created_date").type(JsonFieldType.STRING).description("생성 시간"),
					fieldWithPath("data.modified_date").type(JsonFieldType.STRING).description("수정 시간"))));

		//verify
		verify(noticeFacade, times(1)).changeCoffeeChat(any(NoticeDto.UpdateCategoryRequest.class));
	}

	@Test
	@WithMockUser
	@DisplayName("공지 카테고리를 QNA으로 변경 성공 시 200 OK와 성공 응답을 반환한다.")
	void testChangeQNA() throws Exception {
		NoticeDto.UpdateCategoryRequest request = NoticeDto.UpdateCategoryRequest.builder()
			.noticeToken("ntc_adwag22gsdv")
			.build();

		NoticeDto.FindResponse response = NoticeDto.FindResponse.builder()
			.noticeTitle("환불 관련 공지입니다.")
			.noticeToken("ntc_dawdawdwa")
			.noticeContent("Lets roll out")
			.noticeCategory(Notice.NoticeCategory.QNA)
			.createdDate(LocalDateTime.now())
			.modifiedDate(LocalDateTime.now())
			.build();

		String jsonRequest = objectMapper.writeValueAsString(request);

		doReturn(response).when(noticeFacade).changeQNA(any(NoticeDto.UpdateCategoryRequest.class));

		//when
		ResultActions resultActions = mockMvc.perform(
			RestDocumentationRequestBuilders.put("/api/v1/notices/change-qna")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(jsonRequest));

		//then
		resultActions.andExpect(status().is(NOTICE_UPDATED.getStatus().value()))
			.andDo(document("notice-change-qna", getDocumentRequest(), getDocumentResponse(),
				requestFields(fieldWithPath("notice_token").type(JsonFieldType.STRING).description("공지 토큰")),
				responseFields(fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
					fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
					fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답"),
					fieldWithPath("data.notice_title").type(JsonFieldType.STRING).description("공지 제목"),
					fieldWithPath("data.notice_content").type(JsonFieldType.STRING).description("공지 내용"),
					fieldWithPath("data.notice_token").type(JsonFieldType.STRING).description("공지 토큰"),
					fieldWithPath("data.notice_category").type(JsonFieldType.STRING).description("일반 공지"),
					fieldWithPath("data.created_date").type(JsonFieldType.STRING).description("생성 시간"),
					fieldWithPath("data.modified_date").type(JsonFieldType.STRING).description("수정 시간"))));

		//verify
		verify(noticeFacade, times(1)).changeQNA(any(NoticeDto.UpdateCategoryRequest.class));
	}
}

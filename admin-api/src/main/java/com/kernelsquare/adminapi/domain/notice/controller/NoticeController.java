package com.kernelsquare.adminapi.domain.notice.controller;

import static com.kernelsquare.core.common_response.response.code.NoticeResponseCode.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kernelsquare.adminapi.domain.notice.dto.NoticeDto;
import com.kernelsquare.adminapi.domain.notice.service.NoticeFacade;
import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;
import com.kernelsquare.core.dto.PageResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NoticeController {
	private final NoticeFacade noticeFacade;

	@PostMapping("/notices")
	public ResponseEntity<ApiResponse<NoticeDto.FindResponse>> createNotice(
		@Valid @RequestBody NoticeDto.CreateRequest request) {
		NoticeDto.FindResponse findResponse = noticeFacade.createNotice(request);
		return ResponseEntityFactory.toResponseEntity(NOTICE_FOUND, findResponse);
	}

	@PutMapping("/notices")
	public ResponseEntity<ApiResponse<NoticeDto.FindResponse>> updateNotice(
		@Valid @RequestBody NoticeDto.UpdateRequest request) {
		NoticeDto.FindResponse findResponse = noticeFacade.updateNotice(request);
		return ResponseEntityFactory.toResponseEntity(NOTICE_UPDATED, findResponse);
	}

	@PutMapping("/notices/change-general")
	public ResponseEntity<ApiResponse<NoticeDto.FindResponse>> changeGeneral(
		@Valid @RequestBody NoticeDto.UpdateCategoryRequest request) {
		NoticeDto.FindResponse findResponse = noticeFacade.changeGeneral(request);
		return ResponseEntityFactory.toResponseEntity(NOTICE_UPDATED, findResponse);
	}

	@PutMapping("/notices/change-qna")
	public ResponseEntity<ApiResponse<NoticeDto.FindResponse>> changeQNA(
		@Valid @RequestBody NoticeDto.UpdateCategoryRequest request) {
		NoticeDto.FindResponse findResponse = noticeFacade.changeQNA(request);
		return ResponseEntityFactory.toResponseEntity(NOTICE_UPDATED, findResponse);
	}

	@PutMapping("/notices/change-coffee-chat")
	public ResponseEntity<ApiResponse<NoticeDto.FindResponse>> changeCoffeeChat(
		@Valid @RequestBody NoticeDto.UpdateCategoryRequest request) {
		NoticeDto.FindResponse findResponse = noticeFacade.changeCoffeeChat(request);
		return ResponseEntityFactory.toResponseEntity(NOTICE_UPDATED, findResponse);
	}

	@DeleteMapping("/notices")
	public ResponseEntity<ApiResponse> deleteNotice(@Valid @RequestBody NoticeDto.DeleteRequest request) {
		noticeFacade.deleteNotice(request);
		return ResponseEntityFactory.toResponseEntity(NOTICE_DELETED);
	}

	@GetMapping("/notices/{noticeToken}")
	public ResponseEntity<ApiResponse<NoticeDto.FindResponse>> findNotice(
		@Valid @PathVariable String noticeToken) {
		NoticeDto.FindResponse findResponse = noticeFacade.findNotice(noticeToken);
		return ResponseEntityFactory.toResponseEntity(NOTICE_FOUND, findResponse);
	}

	@GetMapping("/notices")
	public ResponseEntity<ApiResponse<PageResponse<NoticeDto.FindAllResponse>>> findAllNotices
		(@PageableDefault(page = 0, size = 5, sort = "createdDate", direction = Sort.Direction.DESC)
		Pageable pageable) {
		PageResponse pageResponse = noticeFacade.findAllNotice(pageable);
		return ResponseEntityFactory.toResponseEntity(NOTICE_ALL_FOUND, pageResponse);
	}
}

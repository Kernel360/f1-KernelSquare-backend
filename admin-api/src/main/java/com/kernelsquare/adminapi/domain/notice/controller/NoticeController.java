package com.kernelsquare.adminapi.domain.notice.controller;

import static com.kernelsquare.core.common_response.response.code.NoticeResponseCode.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NoticeController {
	private final NoticeFacade noticeFacade;

	@PostMapping("/notices")
	public ResponseEntity<ApiResponse<NoticeDto.SingleResponse>> createNotice(
		@Valid @RequestBody NoticeDto.CreateRequest request) {
		NoticeDto.SingleResponse singleResponse = noticeFacade.createNotice(request);
		return ResponseEntityFactory.toResponseEntity(NOTICE_FOUND, singleResponse);
	}

	@PutMapping("/notices")
	public ResponseEntity<ApiResponse<NoticeDto.SingleResponse>> updateNotice(
		@Valid @RequestBody NoticeDto.UpdateRequest request) {
		NoticeDto.SingleResponse singleResponse = noticeFacade.updateNotice(request);
		return ResponseEntityFactory.toResponseEntity(NOTICE_UPDATED, singleResponse);
	}

	@PutMapping("/notices/change-general")
	public ResponseEntity<ApiResponse<NoticeDto.SingleResponse>> changeGeneral(
		@Valid @RequestBody NoticeDto.UpdateCategoryRequest request) {
		NoticeDto.SingleResponse singleResponse = noticeFacade.changeGeneral(request);
		return ResponseEntityFactory.toResponseEntity(NOTICE_UPDATED, singleResponse);
	}

	@PutMapping("/notices/change-qna")
	public ResponseEntity<ApiResponse<NoticeDto.SingleResponse>> changeQNA(
		@Valid @RequestBody NoticeDto.UpdateCategoryRequest request) {
		NoticeDto.SingleResponse singleResponse = noticeFacade.changeQNA(request);
		return ResponseEntityFactory.toResponseEntity(NOTICE_UPDATED, singleResponse);
	}

	@PutMapping("/notices/change-coffee-chat")
	public ResponseEntity<ApiResponse<NoticeDto.SingleResponse>> changeCoffeeChat(
		@Valid @RequestBody NoticeDto.UpdateCategoryRequest request) {
		NoticeDto.SingleResponse singleResponse = noticeFacade.changeCoffeeChat(request);
		return ResponseEntityFactory.toResponseEntity(NOTICE_UPDATED, singleResponse);
	}

	@DeleteMapping("/notices")
	public ResponseEntity<ApiResponse> deleteNotice(@Valid @RequestBody NoticeDto.DeleteRequest request) {
		noticeFacade.deleteNotice(request);
		return ResponseEntityFactory.toResponseEntity(NOTICE_DELETED);
	}

	@GetMapping("/notices")
	public ResponseEntity<ApiResponse<NoticeDto.SingleResponse>> findNotice(
		@Valid @RequestBody NoticeDto.FindRequest request) {
		NoticeDto.SingleResponse singleResponse = noticeFacade.findNotice(request);
		return ResponseEntityFactory.toResponseEntity(NOTICE_FOUND, singleResponse);
	}

	@GetMapping("/notices/all")
	public ResponseEntity<ApiResponse<PageResponse<NoticeDto.FindAllResponse>>> findAllNotices
		(@PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.DESC)
		Pageable pageable) {
		PageResponse pageResponse = noticeFacade.findAllNotice(pageable);
		return ResponseEntityFactory.toResponseEntity(NOTICE_ALL_FOUND, pageResponse);
	}
}

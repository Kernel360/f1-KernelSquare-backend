package com.kernelsquare.memberapi.domain.notice.controller;

import static com.kernelsquare.core.common_response.response.code.NoticeResponseCode.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kernelsquare.core.common_response.ApiResponse;
import com.kernelsquare.core.common_response.ResponseEntityFactory;
import com.kernelsquare.core.dto.PageResponse;
import com.kernelsquare.memberapi.domain.notice.dto.NoticeDto;
import com.kernelsquare.memberapi.domain.notice.service.NoticeFacade;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NoticeController {
	private final NoticeFacade noticeFacade;

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

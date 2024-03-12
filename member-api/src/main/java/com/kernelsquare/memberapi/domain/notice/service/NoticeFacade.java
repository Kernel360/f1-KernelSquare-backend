package com.kernelsquare.memberapi.domain.notice.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kernelsquare.core.dto.PageResponse;
import com.kernelsquare.domainmysql.domain.notice.info.NoticeInfo;
import com.kernelsquare.domainmysql.domain.notice.service.NoticeService;
import com.kernelsquare.memberapi.domain.notice.dto.NoticeDto;
import com.kernelsquare.memberapi.domain.notice.mapper.NoticeDtoMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeFacade {
	private final NoticeService noticeService;
	private final NoticeDtoMapper noticeDtoMapper;

	public NoticeDto.FindResponse findNotice(String noticeToken) {
		NoticeInfo noticeInfo = noticeService.findNotice(noticeToken);
		return noticeDtoMapper.toSingleResponse(noticeInfo);
	}

	public PageResponse findAllNotice(Pageable pageable) {
		Page<NoticeInfo> allNoticeInfo = noticeService.findAllNotice(pageable);
		List<NoticeDto.FindAllResponse> findAllResponses = allNoticeInfo.getContent().stream()
			.map(info -> noticeDtoMapper.toFindAllResponse(info))
			.toList();
		return PageResponse.of(pageable, allNoticeInfo, findAllResponses);
	}
}


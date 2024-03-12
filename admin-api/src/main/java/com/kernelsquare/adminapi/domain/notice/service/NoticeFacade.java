package com.kernelsquare.adminapi.domain.notice.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kernelsquare.adminapi.domain.notice.dto.NoticeDto;
import com.kernelsquare.adminapi.domain.notice.mapper.NoticeDtoMapper;
import com.kernelsquare.core.dto.PageResponse;
import com.kernelsquare.domainmysql.domain.notice.info.NoticeInfo;
import com.kernelsquare.domainmysql.domain.notice.service.NoticeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeFacade {
	private final NoticeService noticeService;
	private final NoticeDtoMapper noticeDtoMapper;

	public NoticeDto.FindResponse createNotice(NoticeDto.CreateRequest request) {
		NoticeInfo noticeInfo = noticeService.createNotice(noticeDtoMapper.toCommand(request));
		return noticeDtoMapper.toSingleResponse(noticeInfo);
	}

	public NoticeDto.FindResponse updateNotice(NoticeDto.UpdateRequest request) {
		NoticeInfo noticeInfo = noticeService.updateNotice(noticeDtoMapper.toCommand(request), request.noticeToken());
		return noticeDtoMapper.toSingleResponse(noticeInfo);
	}

	public NoticeDto.FindResponse changeGeneral(NoticeDto.UpdateCategoryRequest request) {
		NoticeInfo noticeInfo = noticeService.changeGeneral(request.noticeToken());
		return noticeDtoMapper.toSingleResponse(noticeInfo);
	}

	public NoticeDto.FindResponse changeQNA(NoticeDto.UpdateCategoryRequest request) {
		NoticeInfo noticeInfo = noticeService.changeQNA(request.noticeToken());
		return noticeDtoMapper.toSingleResponse(noticeInfo);
	}

	public NoticeDto.FindResponse changeCoffeeChat(NoticeDto.UpdateCategoryRequest request) {
		NoticeInfo noticeInfo = noticeService.changeCoffeeChat(request.noticeToken());
		return noticeDtoMapper.toSingleResponse(noticeInfo);
	}

	public void deleteNotice(NoticeDto.DeleteRequest request) {
		noticeService.deleteNotice(request.noticeToken());
	}

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


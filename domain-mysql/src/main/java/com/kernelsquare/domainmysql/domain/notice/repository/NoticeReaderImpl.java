package com.kernelsquare.domainmysql.domain.notice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.kernelsquare.core.common_response.error.code.NoticeErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.notice.entity.Notice;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NoticeReaderImpl implements NoticeReader {
	private final NoticeRepository noticeRepository;

	@Override
	public Notice findNotice(String noticeToken) {
		return noticeRepository.findNoticeByNoticeToken(noticeToken)
			.orElseThrow(() -> new BusinessException(NoticeErrorCode.NOTICE_NOT_FOUND));
	}

	@Override
	public Page<Notice> findAllNotice(Pageable pageable) {
		return noticeRepository.findAll(pageable);
	}
}

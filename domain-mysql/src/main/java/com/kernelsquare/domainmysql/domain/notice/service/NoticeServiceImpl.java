package com.kernelsquare.domainmysql.domain.notice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kernelsquare.domainmysql.domain.notice.command.NoticeCommand;
import com.kernelsquare.domainmysql.domain.notice.entity.Notice;
import com.kernelsquare.domainmysql.domain.notice.info.NoticeInfo;
import com.kernelsquare.domainmysql.domain.notice.repository.NoticeReader;
import com.kernelsquare.domainmysql.domain.notice.repository.NoticeStore;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {
	private final NoticeStore noticeStore;
	private final NoticeReader noticeReader;

	@Override
	@Transactional
	public NoticeInfo createNotice(NoticeCommand.CreateCommand command) {
		var initNotice = command.toEntity();
		var notice = noticeStore.store(initNotice);
		return NoticeInfo.of(notice);
	}

	@Override
	@Transactional
	public NoticeInfo updateNotice(NoticeCommand.UpdateCommand command, String noticeToken) {
		var notice = noticeReader.findNotice(noticeToken);
		notice.updateTitleAndContent(command.noticeTitle(), command.noticeContent());
		return NoticeInfo.of(notice);
	}

	@Override
	@Transactional
	public void deleteNotice(String noticeToken) {
		noticeStore.delete(noticeToken);
	}

	@Override
	@Transactional(readOnly = true)
	public NoticeInfo findNotice(String noticeToken) {
		var notice = noticeReader.findNotice(noticeToken);
		return NoticeInfo.of(notice);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<NoticeInfo> findAllNotice(Pageable pageable) {
		Page<Notice> noticePage = noticeReader.findAllNotice(pageable);
		return noticePage.map(NoticeInfo::of);
	}

	@Override
	@Transactional
	public NoticeInfo changeGeneral(String noticeToken) {
		var notice = noticeReader.findNotice(noticeToken);
		notice.changeGeneral();
		return NoticeInfo.of(notice);
	}

	@Override
	@Transactional
	public NoticeInfo changeQNA(String noticeToken) {
		var notice = noticeReader.findNotice(noticeToken);
		notice.changeQNA();
		return NoticeInfo.of(notice);
	}

	@Override
	@Transactional
	public NoticeInfo changeCoffeeChat(String noticeToken) {
		var notice = noticeReader.findNotice(noticeToken);
		notice.changeCoffeeChat();
		return NoticeInfo.of(notice);
	}
}

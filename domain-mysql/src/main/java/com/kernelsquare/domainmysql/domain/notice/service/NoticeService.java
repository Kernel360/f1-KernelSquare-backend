package com.kernelsquare.domainmysql.domain.notice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kernelsquare.domainmysql.domain.notice.command.NoticeCommand;
import com.kernelsquare.domainmysql.domain.notice.info.NoticeInfo;

public interface NoticeService {
	NoticeInfo createNotice(NoticeCommand.CreateCommand command);

	NoticeInfo updateNotice(NoticeCommand.UpdateCommand command, String noticeToken);

	NoticeInfo changeGeneral(String noticeToken);

	NoticeInfo changeQNA(String noticeToken);

	NoticeInfo changeCoffeeChat(String noticeToken);

	void deleteNotice(String noticeToken);

	NoticeInfo findNotice(String noticeToken);

	Page<NoticeInfo> findAllNotice(Pageable pageable);
}

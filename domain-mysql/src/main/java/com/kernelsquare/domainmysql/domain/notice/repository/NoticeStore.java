package com.kernelsquare.domainmysql.domain.notice.repository;

import com.kernelsquare.domainmysql.domain.notice.entity.Notice;

public interface NoticeStore {
	Notice store(Notice initNotice);

	void delete(String noticeToken);
}

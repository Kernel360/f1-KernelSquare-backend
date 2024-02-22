package com.kernelsquare.domainmysql.domain.notice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kernelsquare.domainmysql.domain.notice.entity.Notice;

public interface NoticeReader {
	Notice findNotice(String NoticeToken);

	Page<Notice> findAllNotice(Pageable pageable);
}

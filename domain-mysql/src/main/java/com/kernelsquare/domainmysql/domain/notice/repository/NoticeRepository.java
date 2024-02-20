package com.kernelsquare.domainmysql.domain.notice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kernelsquare.domainmysql.domain.notice.entity.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
	Optional<Notice> findNoticeByNoticeToken(String noticeToken);

	void deleteByNoticeToken(String noticeToken);
}
package com.kernelsquare.domainmysql.domain.search.repository;

import com.kernelsquare.domainmysql.domain.coding_meeting.info.CodingMeetingInfo;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import com.kernelsquare.domainmysql.domain.reservation_article.entity.ReservationArticle;
import com.kernelsquare.domainmysql.domain.tech_stack.entity.TechStack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchRepository {
	Page<Question> searchQuestionsByKeyword(Pageable pageable, String keyword);

	Page<TechStack> searchTechStacksByKeyword(Pageable pageable, String keyword);

	Page<ReservationArticle> searchReservationArticlesByKeyword(Pageable pageable, String keyword);

	Page<CodingMeetingInfo.ListInfo> searchCodingMeetingsByKeyword(Pageable pageable, String filter, String keyword);
}

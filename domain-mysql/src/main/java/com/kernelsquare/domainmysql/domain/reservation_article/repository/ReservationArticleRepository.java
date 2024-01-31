package com.kernelsquare.domainmysql.domain.reservation_article.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kernelsquare.domainmysql.domain.reservation_article.entity.ReservationArticle;

public interface ReservationArticleRepository extends JpaRepository<ReservationArticle, Long> {

}

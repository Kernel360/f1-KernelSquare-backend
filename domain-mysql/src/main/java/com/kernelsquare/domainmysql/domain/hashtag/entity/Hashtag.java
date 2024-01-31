package com.kernelsquare.domainmysql.domain.hashtag.entity;

import com.kernelsquare.domainmysql.domain.base.BaseEntity;
import com.kernelsquare.domainmysql.domain.reservation_article.entity.ReservationArticle;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Hashtag")
@Table(name = "hashtag")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hashtag extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, name = "content", columnDefinition = "varchar(30)")
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reservation_article_id", columnDefinition = "bigint", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private ReservationArticle reservationArticle;

	@Builder
	public Hashtag(Long id, String content, ReservationArticle reservationArticle) {
		this.id = id;
		this.content = content;
		this.reservationArticle = reservationArticle;
	}
}

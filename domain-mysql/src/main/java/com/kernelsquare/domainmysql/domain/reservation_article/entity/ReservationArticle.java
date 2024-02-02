package com.kernelsquare.domainmysql.domain.reservation_article.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.kernelsquare.domainmysql.domain.base.BaseEntity;
import com.kernelsquare.domainmysql.domain.hashtag.entity.Hashtag;
import com.kernelsquare.domainmysql.domain.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "ReservationArticle")
@Table(name = "reservation_article")
@Getter
@NoArgsConstructor
public class ReservationArticle extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	@Column(nullable = false, name = "title", columnDefinition = "varchar(50)")
	private String title;

	@Column(nullable = false, name = "content", columnDefinition = "text")
	private String content;

	@Column(nullable = false, name = "start_time", columnDefinition = "datetime")
	private LocalDateTime startTime;

	@OneToMany(mappedBy = "reservationArticle")
	private List<Hashtag> hashtagList;

	@Builder
	public ReservationArticle(Long id, Member member, String title, String content,
		List<Hashtag> hashtagList, LocalDateTime startTime) {
		this.id = id;
		this.member = member;
		this.title = title;
		this.content = content;
		this.hashtagList = hashtagList;
	}

	public void update(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public void addStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}
}

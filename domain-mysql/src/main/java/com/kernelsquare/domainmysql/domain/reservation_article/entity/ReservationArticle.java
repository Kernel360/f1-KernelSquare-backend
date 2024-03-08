package com.kernelsquare.domainmysql.domain.reservation_article.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.DynamicUpdate;

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
@DynamicUpdate
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

	@Column(nullable = false, name = "introduction", columnDefinition = "varchar(200)")
	private String introduction;

	@Column(nullable = false, name = "start_time", columnDefinition = "datetime")
	private LocalDateTime startTime;

	@Column(nullable = false, name = "end_time", columnDefinition = "datetime")
	private LocalDateTime endTime;

	@OneToMany(mappedBy = "reservationArticle")
	private List<Hashtag> hashtagList;

	@Builder
	public ReservationArticle(Long id, Member member, String title, String content, String introduction,
		List<Hashtag> hashtagList, LocalDateTime startTime, LocalDateTime endTime) {
		this.id = id;
		this.member = member;
		this.title = title;
		this.content = content;
		this.introduction = introduction;
		this.hashtagList = hashtagList;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public void update(String title, String content, String introduction) {
		this.title = title;
		this.content = content;
		this.introduction = introduction;
	}

	public void addStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public void addEndTime(LocalDateTime startTime) {
		this.endTime = endTime;
	}
}

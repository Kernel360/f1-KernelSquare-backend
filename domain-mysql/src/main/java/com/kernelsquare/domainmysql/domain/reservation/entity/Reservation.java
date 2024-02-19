package com.kernelsquare.domainmysql.domain.reservation.entity;

import java.time.LocalDateTime;

import com.kernelsquare.domainmysql.domain.base.BaseEntity;
import com.kernelsquare.domainmysql.domain.coffeechat.entity.ChatRoom;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.reservation_article.entity.ReservationArticle;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Reservation")
@Table(name = "reservation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, name = "start_time", columnDefinition = "datetime")
	private LocalDateTime startTime;

	@Column(nullable = false, name = "end_time", columnDefinition = "datetime")
	private LocalDateTime endTime;

	@Column(nullable = false, name = "finished", columnDefinition = "tinyint")
	private Boolean finished;

	@ManyToOne
	@JoinColumn(name = "member_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reservation_article_id", columnDefinition = "bigint", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private ReservationArticle reservationArticle;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "chat_room_id", columnDefinition = "bigint", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private ChatRoom chatRoom;

	@Builder
	public Reservation(
		LocalDateTime startTime,
		LocalDateTime endTime,
		ReservationArticle reservationArticle,
		ChatRoom chatRoom) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.finished = false;
		this.member = null;
		this.reservationArticle = reservationArticle;
		this.chatRoom = chatRoom;
	}

	public void addMember(Member member) {
		this.member = member;
	}

	public void deleteMember() {
		this.member = null;
	}
}

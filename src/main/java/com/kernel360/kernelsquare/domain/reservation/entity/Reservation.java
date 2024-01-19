package com.kernel360.kernelsquare.domain.reservation.entity;

import com.kernel360.kernelsquare.domain.coffeechat.entity.ChatRoom;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.reservation_article.entity.ReservationArticle;
import com.kernel360.kernelsquare.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
}

package com.kernel360.kernelsquare.domain.reservation.entity;

import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.reservation_article.entity.ReservationArticle;
import com.kernel360.kernelsquare.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean finished;

    @ManyToOne
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_article_id", columnDefinition = "bigint", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private ReservationArticle reservationArticle;

}

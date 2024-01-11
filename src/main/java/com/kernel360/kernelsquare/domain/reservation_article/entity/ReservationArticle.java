package com.kernel360.kernelsquare.domain.reservation_article.entity;

import com.kernel360.kernelsquare.domain.hashtag.entity.HashTag;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "reservation_article")
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

    @OneToMany(mappedBy = "reservationArticle")
    private List<HashTag> hashTagList = new ArrayList<>();

    @Builder
    public ReservationArticle(Long id, Member member, String title, String content, List<HashTag> hashTagList) {
        this.id = id;
        this.member = member;
        this.title = title;
        this.content = content;
        this.hashTagList = hashTagList;
    }

    public void update(String title, String content, List<HashTag> hashTagList) {
        this.title = title;
        this.content = content;
        this.hashTagList = hashTagList;
    }
}

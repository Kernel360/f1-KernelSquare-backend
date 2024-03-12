package com.kernelsquare.domainmysql.domain.answer.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.DynamicUpdate;

import com.kernelsquare.domainmysql.domain.base.BaseEntity;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member_answer_vote.entity.MemberAnswerVote;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import com.kernelsquare.domainmysql.domain.rank.entity.Rank;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Answer")
@Table(name = "answer")
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "image_url", columnDefinition = "varchar(1000)")
	private String imageUrl;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", columnDefinition = "bigint", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rank_id", columnDefinition = "smallint", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Rank rank;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id", columnDefinition = "bigint", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Question question;

	@OneToMany(mappedBy = "answer")
	private List<MemberAnswerVote> memberAnswerVote = new ArrayList<>();

	@Column(name = "content", columnDefinition = "text")
	private String content;

	@Column(name = "vote_count", columnDefinition = "smallint")
	private Long voteCount;

	@Builder
	private Answer(Long id, String imageUrl, String content, Long voteCount, Member member, Question question) {
		this.id = id;
		this.content = content;
		this.voteCount = voteCount;
		this.imageUrl = imageUrl;
		this.member = member;
		this.question = question;
	}

	public void update(String content, String imageUrl) {
		this.content = content;
		this.imageUrl = imageUrl;
	}

	public void updateRank(Rank rank) {
		this.rank = rank;
	}

	public String getNullableRankImageUrl() {
		if (Objects.isNull(rank)) {
			return null;
		}
		return rank.getImage_url();
	}

	public Long getNullableRankName() {
		if (Objects.isNull(rank)) {
			return null;
		}
		return rank.getName();
	}
}

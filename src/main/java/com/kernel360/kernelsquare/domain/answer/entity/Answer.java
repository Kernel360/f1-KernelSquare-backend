package com.kernel360.kernelsquare.domain.answer.entity;

import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.member_answer_vote.entity.MemberAnswerVote;
import com.kernel360.kernelsquare.domain.question.entity.Question;
import com.kernel360.kernelsquare.domain.rank.entity.Rank;
import com.kernel360.kernelsquare.global.entity.BaseEntity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "answer")
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "image_url", columnDefinition = "varchar(100)")
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
}

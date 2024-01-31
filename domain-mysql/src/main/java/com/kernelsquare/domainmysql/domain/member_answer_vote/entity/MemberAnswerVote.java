package com.kernelsquare.domainmysql.domain.member_answer_vote.entity;

import com.kernelsquare.domainmysql.domain.answer.entity.Answer;
import com.kernelsquare.domainmysql.domain.base.BaseEntity;
import com.kernelsquare.domainmysql.domain.member.entity.Member;

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

@Entity(name = "MemberAnswerVote")
@Table(name = "member_answer_vote")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberAnswerVote extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, name = "status", columnDefinition = "smallint")
	private Integer status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", columnDefinition = "bigint", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "answer_id", columnDefinition = "bigint", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Answer answer;

	@Builder
	private MemberAnswerVote(Long id, int status, Member member, Answer answer) {
		this.id = id;
		this.status = status;
		this.member = member;
		this.answer = answer;
	}

	public Long getAnswerId() {
		return answer.getId();
	}
}

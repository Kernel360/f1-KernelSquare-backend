package com.kernelsquare.domainmysql.domain.question.entity;

import java.util.List;

import com.kernelsquare.domainmysql.domain.base.BaseEntity;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.question_tech_stack.entity.QuestionTechStack;

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

@Entity(name = "Question")
@Table(name = "question")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, name = "title", columnDefinition = "varchar(50)")
	private String title;

	@Column(nullable = false, name = "content", columnDefinition = "text")
	private String content;

	@Column(name = "image_url", columnDefinition = "varchar(100)")
	private String imageUrl;

	@Column(name = "view_count", columnDefinition = "bigint")
	private Long viewCount;

	@Column(nullable = false, name = "closed_status", columnDefinition = "tinyint")
	private Boolean closedStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", columnDefinition = "bigint", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Member member;

	@OneToMany(mappedBy = "question")
	private List<QuestionTechStack> techStackList;

	@Builder
	public Question(Long id, String title, String content, String imageUrl, Long viewCount, Boolean closedStatus,
		Member member, List<QuestionTechStack> techStackList) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.imageUrl = imageUrl;
		this.viewCount = viewCount;
		this.closedStatus = closedStatus;
		this.member = member;
		this.techStackList = techStackList;
	}

	public void update(String title, String content, String imageUrl) {
		this.title = title;
		this.content = content;
		this.imageUrl = imageUrl;
	}

	public void updateClosedStatus() {
		this.closedStatus = true;
	}

	public void setTechStackList(List<QuestionTechStack> techStackList) {
		this.techStackList = techStackList;
	}
}

package com.kernel360.kernelsquare.domain.question.entity;

import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.question_tech_stack.entity.QuestionTechStack;
import com.kernel360.kernelsquare.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "question")
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
	public Question(String title, String content, String imageUrl, Long viewCount, Boolean closedStatus, Member member, List<QuestionTechStack> techStackList) {
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

	public void setTechStackList(List<QuestionTechStack> techStackList) {
		this.techStackList = techStackList;
	}
}

package com.kernelsquare.domainmysql.domain.question_tech_stack.entity;

import com.kernelsquare.domainmysql.domain.base.BaseEntity;
import com.kernelsquare.domainmysql.domain.question.entity.Question;
import com.kernelsquare.domainmysql.domain.tech_stack.entity.TechStack;

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

@Entity(name = "QuestionTechStack")
@Table(name = "question_tech_stack")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionTechStack extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tech_stack_id", columnDefinition = "bigint", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private TechStack techStack;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id", columnDefinition = "bigint", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Question question;

	@Builder
	public QuestionTechStack(TechStack techStack, Question question) {
		this.techStack = techStack;
		this.question = question;
	}
}

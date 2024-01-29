package com.kernel360.kernelsquare.domain.question_tech_stack.entity;

import com.kernel360.kernelsquare.domain.question.entity.Question;
import com.kernel360.kernelsquare.domain.tech_stack.entity.TechStack;
import com.kernel360.kernelsquare.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "question_tech_stack")
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

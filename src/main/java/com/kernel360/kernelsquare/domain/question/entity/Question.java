package com.kernel360.kernelsquare.domain.question.entity;

import java.time.LocalDateTime;

import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.global.entity.BaseEntity;

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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

	@Column(nullable = false, name = "due_date", columnDefinition = "datetime")
	private LocalDateTime dueDate;

	@Column(name = "image_url", columnDefinition = "varchar(100)")
	private String imageUrl;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by", columnDefinition = "bigint(50)", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Member member;
}

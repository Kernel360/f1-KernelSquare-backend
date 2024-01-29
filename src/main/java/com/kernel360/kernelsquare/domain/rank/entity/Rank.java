package com.kernel360.kernelsquare.domain.rank.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "answer_rank")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rank {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, name = "image_url", columnDefinition = "varchar(100)")
	private String image_url;
}

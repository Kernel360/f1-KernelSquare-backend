package com.kernelsquare.domainmysql.domain.rank.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Rank")
@Table(name = "answer_rank")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rank {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, name = "name", columnDefinition = "smallint")
	private Long name;

	@Column(nullable = false, name = "image_url", columnDefinition = "varchar(1000)")
	private String image_url;
}

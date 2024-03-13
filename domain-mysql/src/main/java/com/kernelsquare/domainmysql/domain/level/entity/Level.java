package com.kernelsquare.domainmysql.domain.level.entity;

import org.hibernate.annotations.DynamicUpdate;

import com.kernelsquare.domainmysql.domain.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Level")
@Table(name = "level")
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Level extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, name = "name", columnDefinition = "smallint")
	private Long name;

	@Column(nullable = false, name = "image_url", columnDefinition = "varchar(1000)")
	private String imageUrl;

	@Column(nullable = false, name = "level_upper_limit", columnDefinition = "bigint")
	private Long levelUpperLimit;

	@Builder
	public Level(Long id, Long name, String imageUrl, Long levelUpperLimit) {
		this.id = id;
		this.name = name;
		this.imageUrl = imageUrl;
		this.levelUpperLimit = levelUpperLimit;
	}

	public void update(Long name, String imageUrl, Long levelUpperLimit) {
		this.name = name;
		this.imageUrl = imageUrl;
		this.levelUpperLimit = levelUpperLimit;
	}
}

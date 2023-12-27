package com.kernel360.kernelsquare.domain.level.entity;

import com.kernel360.kernelsquare.global.entity.BaseEntity;

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

@Entity(name = "level")
@Getter
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Level extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, name = "name", columnDefinition = "smallint")
	private Long name;

	@Column(nullable = false, name = "image_url", columnDefinition = "varchar(100)")
	private String imageUrl;

	@Builder
	public Level(Long name, String imageUrl) {
		this.name = name;
		this.imageUrl = imageUrl;
	}
}

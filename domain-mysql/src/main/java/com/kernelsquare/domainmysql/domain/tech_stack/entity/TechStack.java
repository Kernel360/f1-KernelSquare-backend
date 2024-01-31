package com.kernelsquare.domainmysql.domain.tech_stack.entity;

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

@Entity(name = "TechStack")
@Table(name = "tech_stack")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TechStack extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, name = "skill", columnDefinition = "varchar(20)")
	private String skill;

	@Builder
	public TechStack(Long id, String skill) {
		this.id = id;
		this.skill = skill;
	}

	public void update(String skill) {
		this.skill = skill;
	}
}

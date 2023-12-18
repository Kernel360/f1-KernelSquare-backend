package com.kernel360.kernelsquare.domain.social_login.entity;

import com.kernel360.kernelsquare.global.domain.SocialProvider;
import com.kernel360.kernelsquare.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "social_login")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialLogin extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, name = "email", columnDefinition = "varchar(50)")
	private String email;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false, name = "social_provider", columnDefinition = "varchar(40)")
	private SocialProvider socialProvider;
}

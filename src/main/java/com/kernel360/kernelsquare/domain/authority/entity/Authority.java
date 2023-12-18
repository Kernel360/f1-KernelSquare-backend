package com.kernel360.kernelsquare.domain.authority.entity;

import com.kernel360.kernelsquare.global.domain.AuthorityType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "authority")
@Getter
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false, name = "authority_type", columnDefinition = "varchar(20)")
	private AuthorityType authorityType;
}

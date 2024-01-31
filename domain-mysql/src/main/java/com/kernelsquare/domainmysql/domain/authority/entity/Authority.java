package com.kernelsquare.domainmysql.domain.authority.entity;

import com.kernelsquare.core.type.AuthorityType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Authority")
@Table(name = "authority")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false, name = "authority_type", columnDefinition = "varchar(20)")
	private AuthorityType authorityType;

	@Builder
	public Authority(Long id, AuthorityType authorityType) {
		this.id = id;
		this.authorityType = authorityType;
	}
}

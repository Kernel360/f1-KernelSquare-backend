package com.kernelsquare.domainmysql.domain.authority.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kernelsquare.core.type.AuthorityType;
import com.kernelsquare.domainmysql.domain.authority.entity.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
	Optional<Authority> findAuthorityByAuthorityType(AuthorityType authorityType);
}
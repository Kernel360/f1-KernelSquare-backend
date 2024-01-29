package com.kernel360.kernelsquare.domain.authority.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kernel360.kernelsquare.domain.authority.entity.Authority;
import com.kernel360.kernelsquare.global.domain.AuthorityType;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
	Optional<Authority> findAuthorityByAuthorityType(AuthorityType authorityType);
}
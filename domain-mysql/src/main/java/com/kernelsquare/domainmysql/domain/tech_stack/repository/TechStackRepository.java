package com.kernelsquare.domainmysql.domain.tech_stack.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kernelsquare.domainmysql.domain.tech_stack.entity.TechStack;

public interface TechStackRepository extends JpaRepository<TechStack, Long> {
    Optional<TechStack> findBySkill(String skill);

    boolean existsBySkill(String skill);
}

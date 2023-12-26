package com.kernel360.kernelsquare.domain.tech_stack.repository;

import com.kernel360.kernelsquare.domain.tech_stack.entity.TechStack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TechStackRepository extends JpaRepository<TechStack, Long> {
    Optional<TechStack> findBySkill(String skill);
}

package com.kernelsquare.memberapi.domain.tech_stack.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kernelsquare.memberapi.domain.tech_stack.dto.FindAllTechStacksResponse;
import com.kernelsquare.domainmysql.domain.tech_stack.entity.TechStack;
import com.kernelsquare.domainmysql.domain.tech_stack.repository.TechStackRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TechStackService {
	private final TechStackRepository techStackRepository;

	@Transactional(readOnly = true)
	public FindAllTechStacksResponse findAllTechStacks() {
		List<TechStack> techStackList = techStackRepository.findAll();

		return FindAllTechStacksResponse.from(techStackList);
	}
}

package com.kernelsquare.adminapi.domain.level.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kernelsquare.adminapi.domain.level.dto.CreateLevelRequest;
import com.kernelsquare.adminapi.domain.level.dto.CreateLevelResponse;
import com.kernelsquare.adminapi.domain.level.dto.FindAllLevelResponse;
import com.kernelsquare.adminapi.domain.level.dto.UpdateLevelRequest;
import com.kernelsquare.adminapi.domain.level.dto.UpdateLevelResponse;
import com.kernelsquare.core.common_response.error.code.LevelErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.domainmysql.domain.level.entity.Level;
import com.kernelsquare.domainmysql.domain.level.repository.LevelRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LevelService {
	private final LevelRepository levelRepository;

	public CreateLevelResponse createLevel(CreateLevelRequest createLevelRequest) {
		Level level = CreateLevelRequest.toEntity(createLevelRequest);
		try {
			levelRepository.saveAndFlush(level);
		} catch (DataIntegrityViolationException e) {
			throw new BusinessException(LevelErrorCode.LEVEL_ALREADY_EXISTED);
		}
		return CreateLevelResponse.from(level);
	}

	@Transactional(readOnly = true)
	public FindAllLevelResponse findAllLevel() {
		List<Level> levelList = levelRepository.findAll();
		return FindAllLevelResponse.from(levelList);
	}

	@Transactional
	public void deleteLevel(Long levelId) {
		levelRepository.deleteById(levelId);
	}

	@Transactional
	public UpdateLevelResponse updateLevel(Long levelId, UpdateLevelRequest updateLevelRequest) {
		Level level = levelRepository.findById(levelId)
			.orElseThrow(() -> new BusinessException(LevelErrorCode.LEVEL_NOT_FOUND));

		levelRepository.findByNameAndIdNot(level.getName(), levelId)
			.ifPresent(l -> {
				throw new BusinessException(LevelErrorCode.LEVEL_ALREADY_EXISTED);
			});

		level.update(updateLevelRequest.name(), updateLevelRequest.imageUrl(), updateLevelRequest.levelUpperLimit());

		return UpdateLevelResponse.from(level);
	}

}

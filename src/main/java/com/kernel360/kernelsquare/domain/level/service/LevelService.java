package com.kernel360.kernelsquare.domain.level.service;

import com.kernel360.kernelsquare.domain.level.dto.CreateLevelRequest;
import com.kernel360.kernelsquare.domain.level.dto.CreateLevelResponse;
import com.kernel360.kernelsquare.domain.level.entity.Level;
import com.kernel360.kernelsquare.domain.level.repository.LevelRepository;
import com.kernel360.kernelsquare.domain.member.repository.MemberRepository;
import com.kernel360.kernelsquare.global.common_response.error.code.LevelErrorCode;
import com.kernel360.kernelsquare.global.common_response.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LevelService {

    private final LevelRepository levelRepository;

    public CreateLevelResponse createLevel(CreateLevelRequest createLevelRequest) {
        Level level = CreateLevelRequest.toEntity(createLevelRequest);
        try {
            levelRepository.save(level);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(LevelErrorCode.LEVEL_ALREADY_EXISTED);
        }
        return CreateLevelResponse.of(level);

    }

}

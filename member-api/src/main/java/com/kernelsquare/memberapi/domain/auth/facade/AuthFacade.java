package com.kernelsquare.memberapi.domain.auth.facade;

import com.kernelsquare.domainmysql.domain.auth.info.AuthInfo;
import com.kernelsquare.memberapi.domain.auth.dto.AuthDto;
import com.kernelsquare.memberapi.domain.auth.mapper.AuthDtoMapper;
import com.kernelsquare.memberapi.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthFacade {
    private final AuthService authService;
    private final AuthDtoMapper authDtoMapper;

    public AuthDto.LoginResponse login(AuthDto.LoginRequest request) {
        AuthInfo.LoginInfo loginInfo = authService.login(authDtoMapper.toCommand(request));
        return authDtoMapper.of(loginInfo);
    }
}

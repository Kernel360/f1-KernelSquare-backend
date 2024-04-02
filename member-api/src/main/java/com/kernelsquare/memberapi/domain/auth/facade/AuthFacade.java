package com.kernelsquare.memberapi.domain.auth.facade;

import com.kernelsquare.domainmysql.domain.auth.info.AuthInfo;
import com.kernelsquare.domainmysql.domain.member.info.MemberInfo;
import com.kernelsquare.memberapi.domain.auth.dto.AuthDto;
import com.kernelsquare.memberapi.domain.auth.dto.MemberAdapter;
import com.kernelsquare.memberapi.domain.auth.mapper.AuthDtoMapper;
import com.kernelsquare.memberapi.domain.auth.service.AuthService;
import com.kernelsquare.memberapi.domain.auth.service.MemberDetailService;
import com.kernelsquare.memberapi.domain.auth.service.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthFacade {
    private final AuthService authService;
    private final TokenProvider tokenProvider;
    private final AuthDtoMapper authDtoMapper;
    private final MemberDetailService memberDetailService;

    public AuthDto.LoginResponse login(AuthDto.LoginRequest request) {
        MemberInfo memberInfo = authService.login(authDtoMapper.toCommand(request));
//        MemberAdapter memberAdapter = (MemberAdapter) memberDetailService.loadUserByUsername(memberInfo.getId().toString());
        AuthInfo.LoginInfo loginInfo = tokenProvider.createToken(memberInfo);
        return authDtoMapper.of(loginInfo);
    }
}

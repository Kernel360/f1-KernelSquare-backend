package com.kernelsquare.adminapi.domain.member.facade;

import com.kernelsquare.adminapi.domain.member.dto.MemberDto;
import com.kernelsquare.adminapi.domain.member.mapper.MemberDtoMapper;
import com.kernelsquare.adminapi.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberFacade {
    private final MemberDtoMapper memberDtoMapper;
    private final MemberService memberService;
    public void updateMemberAuthority(MemberDto.UpdateAuthorityRequest request) {
         memberService.updateMemberAuthority(memberDtoMapper.toCommand(request));
    }
}

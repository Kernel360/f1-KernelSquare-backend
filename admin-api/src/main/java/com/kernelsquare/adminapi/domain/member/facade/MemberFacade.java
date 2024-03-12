package com.kernelsquare.adminapi.domain.member.facade;

import com.kernelsquare.adminapi.domain.member.dto.MemberDto;
import com.kernelsquare.adminapi.domain.member.mapper.MemberDtoMapper;
import com.kernelsquare.domainmysql.domain.member.info.MemberInfo;
import com.kernelsquare.domainmysql.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberFacade {
    private final MemberDtoMapper memberDtoMapper;
    private final MemberService memberService;

    public MemberDto.FindResponse findMember(Long memberId) {
        MemberInfo memberInfo = memberService.findMember(memberId);
        return memberDtoMapper.toFindResponse(memberInfo);
    }

    public void deleteMember(Long memberId) {
        memberService.deleteMember(memberId);
    }

    public void updateMemberAuthority(MemberDto.UpdateAuthorityRequest request) {
         memberService.updateMemberAuthority(memberDtoMapper.toCommand(request));
    }

    public MemberDto.FindResponse updateMemberNickname(MemberDto.UpdateNicknameRequest request) {
        MemberInfo memberInfo = memberService.updateMemberNickname(memberDtoMapper.toCommand(request));
        return memberDtoMapper.toFindResponse(memberInfo);
    }
}

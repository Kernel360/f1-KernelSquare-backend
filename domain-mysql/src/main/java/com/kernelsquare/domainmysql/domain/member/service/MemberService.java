package com.kernelsquare.domainmysql.domain.member.service;

import com.kernelsquare.domainmysql.domain.member.command.MemberCommand;
import com.kernelsquare.domainmysql.domain.member.info.MemberInfo;

public interface MemberService {
    MemberInfo findMember(Long memberId);

    void deleteMember(Long memberId);

    void updateMemberAuthority(MemberCommand.UpdateAuthorityCommand command);

    MemberInfo updateMemberNickname(MemberCommand.UpdateNicknameCommand command);
}

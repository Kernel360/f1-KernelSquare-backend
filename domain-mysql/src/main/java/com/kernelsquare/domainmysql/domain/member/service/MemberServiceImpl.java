package com.kernelsquare.domainmysql.domain.member.service;

import com.kernelsquare.core.common_response.error.code.MemberErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.core.type.AuthorityType;
import com.kernelsquare.domainmysql.domain.authority.entity.Authority;
import com.kernelsquare.domainmysql.domain.authority.repository.AuthorityReader;
import com.kernelsquare.domainmysql.domain.member.command.MemberCommand;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member.info.MemberInfo;
import com.kernelsquare.domainmysql.domain.member.repository.MemberReader;
import com.kernelsquare.domainmysql.domain.member.repository.MemberStore;
import com.kernelsquare.domainmysql.domain.member_authority.entity.MemberAuthority;
import com.kernelsquare.domainmysql.domain.member_authority.repository.MemberAuthorityStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberReader memberReader;
    private final MemberStore memberStore;
    private final MemberAuthorityStore memberAuthorityStore;
    private final AuthorityReader authorityReader;

    @Override
    @Transactional(readOnly = true)
    public MemberInfo findMember(Long memberId) {
        Member member = memberReader.findMember(memberId);
        return MemberInfo.from(member);
    }

    @Override
    @Transactional
    public void deleteMember(Long memberId) {
        memberStore.deleteMember(memberId);
    }

    @Override
    @Transactional
    public void updateMemberAuthority(MemberCommand.UpdateAuthorityCommand command) {
        Member member = memberReader.findMember(command.memberId());

        memberAuthorityStore.deleteMemberAuthorities(member.getId());

        Authority roleUser = authorityReader.findAuthority(AuthorityType.ROLE_USER);
        Authority roleMentor = authorityReader.findAuthority(AuthorityType.ROLE_MENTOR);
        Authority roleAdmin = authorityReader.findAuthority(AuthorityType.ROLE_ADMIN);

        List<MemberAuthority> authorities = createAuthorities(member, command.authorityType(), roleUser, roleMentor, roleAdmin);

        for (MemberAuthority authority : authorities) {
            memberAuthorityStore.store(authority);
        }
    }

    @Override
    @Transactional
    public MemberInfo updateMemberNickname(MemberCommand.UpdateNicknameCommand command) {
        Member member = memberReader.findMember(command.memberId());
        member.updateNickname(command.nickname());
        return MemberInfo.from(member);
    }

    private List<MemberAuthority> createAuthorities(Member member, AuthorityType authorityType
        , Authority roleUser, Authority roleMentor, Authority roleAdmin) {
        switch (authorityType) {
            case AuthorityType.ROLE_USER -> {
                return List.of(MemberAuthority.of(member, roleUser));
            }
            case AuthorityType.ROLE_MENTOR -> {
                return List.of(
                    MemberAuthority.of(member, roleUser),
                    MemberAuthority.of(member, roleMentor)
                );
            }
            case AuthorityType.ROLE_ADMIN -> {
                return List.of(
                    MemberAuthority.of(member, roleUser),
                    MemberAuthority.of(member, roleMentor),
                    MemberAuthority.of(member, roleAdmin)
                );
            }
            default -> throw new BusinessException(MemberErrorCode.AUTHORITY_TYPE_NOT_VALID);
        }
    }
}

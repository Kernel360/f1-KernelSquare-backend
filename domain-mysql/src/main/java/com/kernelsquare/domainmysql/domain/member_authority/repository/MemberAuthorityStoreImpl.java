package com.kernelsquare.domainmysql.domain.member_authority.repository;

import com.kernelsquare.domainmysql.domain.member_authority.entity.MemberAuthority;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberAuthorityStoreImpl implements MemberAuthorityStore {
    private final MemberAuthorityRepository memberAuthorityRepository;

    @Override
    public MemberAuthority store(MemberAuthority initMemberAuthority) {
        return memberAuthorityRepository.save(initMemberAuthority);
    }

    @Override
    public void deleteMemberAuthorities(Long memberId) {
        memberAuthorityRepository.deleteAllByMemberId(memberId);
    }
}

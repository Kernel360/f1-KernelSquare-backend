package com.kernelsquare.domainmysql.domain.member.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberStoreImpl implements MemberStore {
    private final MemberRepository memberRepository;

    @Override
    public void deleteMember(Long memberId) {
        memberRepository.deleteById(memberId);
    }
}

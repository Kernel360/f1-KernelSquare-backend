package com.kernelsquare.memberapi.domain.auth.dto;

import com.kernelsquare.domainmysql.domain.member.entity.Member;

public class MemberAdapter extends MemberDetails {
    private Member member;

    public MemberAdapter(Member member) {
        super(member);
        this.member = member;
    }
}

package com.kernelsquare.domainmysql.domain.member.command;

import com.kernelsquare.core.type.AuthorityType;
import lombok.Builder;

public class MemberCommand {
    @Builder
    public record UpdateAuthorityCommand(
        Long memberId,
        AuthorityType authorityType
    ) {}
}

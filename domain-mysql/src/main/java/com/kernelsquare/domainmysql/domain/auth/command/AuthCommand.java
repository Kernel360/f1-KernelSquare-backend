package com.kernelsquare.domainmysql.domain.auth.command;


import lombok.Builder;

public class AuthCommand {
    @Builder
    public record LoginMember(
        String email,
        String password
    ) {}
}

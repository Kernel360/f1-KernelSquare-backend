package com.kernelsquare.domainmysql.domain.auth.command;


public class AuthCommand {
    public record LoginMember(
        String email,
        String password
    ) {}
}

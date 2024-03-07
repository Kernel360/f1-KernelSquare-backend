package com.kernelsquare.domainmongodb.domain.alert.command;

import lombok.Builder;

public class AlertCommand {
    @Builder
    public record FindCommand(
        String memberId
    ) {}
}

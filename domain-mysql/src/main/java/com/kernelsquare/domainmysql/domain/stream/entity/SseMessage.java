package com.kernelsquare.domainmysql.domain.stream.entity;

import lombok.Getter;

@Getter
public class SseMessage {
    private String memberId;
    private Object message;
}

package com.kernel360.kernelsquare.domain.coffeechat.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MongoMessageType {
    ENTER, TALK, LEAVE, EXPIRE
}

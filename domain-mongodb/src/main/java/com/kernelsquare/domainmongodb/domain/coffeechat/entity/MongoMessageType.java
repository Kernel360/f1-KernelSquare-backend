package com.kernelsquare.domainmongodb.domain.coffeechat.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MongoMessageType {
	ENTER, TALK, CODE, LEAVE, EXPIRE
}

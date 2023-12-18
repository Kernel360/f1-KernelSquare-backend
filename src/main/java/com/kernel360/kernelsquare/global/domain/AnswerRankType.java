package com.kernel360.kernelsquare.global.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AnswerRankType {
	FIRST("first"),
	SECOND("second"),
	THIRD("third");

	private final String description;
}

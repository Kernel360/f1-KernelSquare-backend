package com.kernelsquare.core.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthorityType {
	ROLE_ADMIN("ROLE_ADMIN"),
	ROLE_USER("ROLE_USER"),
	ROLE_MENTOR("ROLE_MENTOR");

	private final String description;
}

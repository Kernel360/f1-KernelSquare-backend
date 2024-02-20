package com.kernelsquare.core.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MemberAnswerVoteStatus implements ServiceStatus {
	// error
	MEMBER_ANSWER_VOTE_NOT_FOUND(2600),
	MEMBER_ANSWER_VOTE_SELF_IMPOSSIBLE(2601),
	MEMBER_ANSWER_VOTE_DUPLICATION(2602),

	// success
	MEMBER_ANSWER_VOTE_CREATED(2640),
	MEMBER_ANSWER_VOTE_DELETED(2641);

	private final Integer code;

	@Override
	public Integer getServiceStatus() {
		return code;
	}
}

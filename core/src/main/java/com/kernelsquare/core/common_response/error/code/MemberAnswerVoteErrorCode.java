package com.kernelsquare.core.common_response.error.code;

import org.springframework.http.HttpStatus;

import com.kernelsquare.core.common_response.service.code.MemberAnswerVoteStatus;
import com.kernelsquare.core.common_response.service.code.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MemberAnswerVoteErrorCode implements ErrorCode {
	MEMBER_ANSWER_VOTE_NOT_FOUND(
		HttpStatus.NOT_FOUND, MemberAnswerVoteStatus.MEMBER_ANSWER_VOTE_NOT_FOUND, "해당 투표는 존재하지 않습니다."),
	MEMBER_ANSWER_VOTE_SELF_IMPOSSIBLE(
			HttpStatus.BAD_REQUEST, MemberAnswerVoteStatus.MEMBER_ANSWER_VOTE_SELF_IMPOSSIBLE, "본인 답변에는 투표할 수 없습니다."),
	MEMBER_ANSWER_VOTE_DUPLICATION(
			HttpStatus.CONFLICT, MemberAnswerVoteStatus.MEMBER_ANSWER_VOTE_DUPLICATION, "중복 투표입니다.");

	private final HttpStatus code;
	private final ServiceStatus serviceStatus;
	private final String msg;

	@Override
	public HttpStatus getStatus() {
		return code;
	}

	@Override
	public Integer getCode() {
		return serviceStatus.getServiceStatus();
	}

	@Override
	public String getMsg() {
		return msg;
	}
}

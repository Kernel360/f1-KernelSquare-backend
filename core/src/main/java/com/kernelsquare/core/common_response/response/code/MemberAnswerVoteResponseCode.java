package com.kernelsquare.core.common_response.response.code;

import org.springframework.http.HttpStatus;

import com.kernelsquare.core.common_response.service.code.MemberAnswerVoteStatus;
import com.kernelsquare.core.common_response.service.code.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MemberAnswerVoteResponseCode implements ResponseCode {
	MEMBER_ANSWER_VOTE_CREATED(HttpStatus.OK, MemberAnswerVoteStatus.MEMBER_ANSWER_VOTE_CREATED, "투표 생성 성공"),
	MEMBER_ANSWER_VOTE_DELETED(HttpStatus.OK, MemberAnswerVoteStatus.MEMBER_ANSWER_VOTE_DELETED, "투표 삭제 성공");

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

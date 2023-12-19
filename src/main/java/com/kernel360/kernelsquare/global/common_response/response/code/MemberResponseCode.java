package com.kernel360.kernelsquare.global.common_response.response.code;

import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MemberResponseCode implements ResponseCode {
	MEMBER_FOUND(HttpStatus.OK, "회원 정보 조회 성공"),
	MEMBER_PASSWORD_UPDATED(HttpStatus.OK, "비밀번호 수정 완료"),
	MEMBER_INFO_UPDATED(HttpStatus.OK, "회원 정보 수정 성공"),
	MEMBER_DELETED(HttpStatus.OK, "회원 탈퇴 성공");

	private final HttpStatus code;
	private final String msg;

	@Override
	public HttpStatus getStatus() {
		return code;
	}

	@Override
	public String getMsg() {
		return msg;
	}
}

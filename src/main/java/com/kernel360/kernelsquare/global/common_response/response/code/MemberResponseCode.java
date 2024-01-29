package com.kernel360.kernelsquare.global.common_response.response.code;

import org.springframework.http.HttpStatus;

import com.kernel360.kernelsquare.global.common_response.service.code.MemberServiceStatus;
import com.kernel360.kernelsquare.global.common_response.service.code.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MemberResponseCode implements ResponseCode {
	MEMBER_FOUND(HttpStatus.OK, MemberServiceStatus.MEMBER_FOUND, "회원 정보 조회 성공"),
	MEMBER_PASSWORD_UPDATED(HttpStatus.OK, MemberServiceStatus.MEMBER_PASSWORD_UPDATED, "비밀번호 수정 성공"),
	MEMBER_PROFILE_UPDATED(HttpStatus.OK, MemberServiceStatus.MEMBER_PROFILE_UPDATED, "회원 프로필 수정 성공"),
	MEMBER_INTRODUCTION_UPDATED(HttpStatus.OK, MemberServiceStatus.MEMBER_INTRODUCTION_UPDATED, "회원 소개 수정 성공"),
	MEMBER_DELETED(HttpStatus.OK, MemberServiceStatus.MEMBER_DELETED, "회원 탈퇴 성공");

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

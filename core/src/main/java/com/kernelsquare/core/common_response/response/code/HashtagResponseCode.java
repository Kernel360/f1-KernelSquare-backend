package com.kernelsquare.core.common_response.response.code;

import org.springframework.http.HttpStatus;

import com.kernelsquare.core.common_response.service.code.HashtagServiceStatus;
import com.kernelsquare.core.common_response.service.code.ServiceStatus;

import lombok.RequiredArgsConstructor;

public class HashtagResponseCode {
	@RequiredArgsConstructor
	public enum HashTagResponseCode implements ResponseCode {
		HASHTAG_ALL_FOUND(HttpStatus.OK, HashtagServiceStatus.HASHTAG_ALL_FOUND, "모든 해시태그를 조회했습니다."),
		HASHTAG_DELETED(HttpStatus.OK, HashtagServiceStatus.HASHTAG_DELETED, "해시태그를 삭제했습니다.");

		private final HttpStatus httpStatus;
		private final ServiceStatus serviceStatus;
		private final String msg;

		@Override
		public HttpStatus getStatus() {
			return httpStatus;
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
}

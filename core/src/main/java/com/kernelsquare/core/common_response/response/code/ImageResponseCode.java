package com.kernelsquare.core.common_response.response.code;

import org.springframework.http.HttpStatus;

import com.kernelsquare.core.common_response.service.code.ImageServiceStatus;
import com.kernelsquare.core.common_response.service.code.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ImageResponseCode implements ResponseCode {
	IMAGE_UPLOAD_COMPLETED(HttpStatus.OK, ImageServiceStatus.IMAGE_UPLOAD_COMPLETED, "이미지 업로드 완료"),
	IMAGE_DELETED(HttpStatus.OK, ImageServiceStatus.IMAGE_DELETED, "이미지 삭제 완료"),
	IMAGE_ALL_FOUND(HttpStatus.OK, ImageServiceStatus.IMAGE_ALL_FOUND, "날짜별 이미지 모두 조회 성공");

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

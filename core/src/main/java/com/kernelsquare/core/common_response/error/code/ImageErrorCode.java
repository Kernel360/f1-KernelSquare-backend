package com.kernelsquare.core.common_response.error.code;

import org.springframework.http.HttpStatus;

import com.kernelsquare.core.common_response.service.code.ImageServiceStatus;
import com.kernelsquare.core.common_response.service.code.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ImageErrorCode implements ErrorCode {
	IMAGE_IS_EMPTY(HttpStatus.BAD_REQUEST, ImageServiceStatus.IMAGE_IS_EMPTY, "빈 이미지"),
	IMAGE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, ImageServiceStatus.IMAGE_UPLOAD_FAILED, "이미지 업로드 실패"),
	FILE_EXTENSION_NOT_VALID(HttpStatus.BAD_REQUEST, ImageServiceStatus.FILE_EXTENSION_NOT_VALID, "유효하지 않는 파일 확장자"),
	FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, ImageServiceStatus.FILE_SIZE_EXCEEDED, "파일 크기 초과");

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

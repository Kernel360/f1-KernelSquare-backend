package com.kernelsquare.core.common_response.service.code;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ImageServiceStatus implements ServiceStatus {
	//error
	IMAGE_IS_EMPTY(2400),
	IMAGE_UPLOAD_FAILED(2401),
	FILE_EXTENSION_NOT_VALID(2402),
	FILE_SIZE_EXCEEDED(2403),

	//success
	IMAGE_UPLOAD_COMPLETED(2440),
	IMAGE_DELETED(2441),
	IMAGE_ALL_FOUND(2442);

	private final Integer code;

	@Override
	public Integer getServiceStatus() {
		return code;
	}
}

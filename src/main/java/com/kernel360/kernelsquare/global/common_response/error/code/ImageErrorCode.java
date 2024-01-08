package com.kernel360.kernelsquare.global.common_response.error.code;

import com.kernel360.kernelsquare.global.common_response.service.code.ImageServiceStatus;
import com.kernel360.kernelsquare.global.common_response.service.code.ServiceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ImageErrorCode implements ErrorCode {
    IMAGE_IS_EMPTY(HttpStatus.BAD_REQUEST, ImageServiceStatus.IMAGE_IS_EMPTY, "빈 이미지"),
    IMAGE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, ImageServiceStatus.IMAGE_UPLOAD_FAILED, "이미지 업로드 실패");

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

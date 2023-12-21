package com.kernel360.kernelsquare.global.common_response.error.code;

import com.kernel360.kernelsquare.global.common_response.service.code.QuestionServiceStatus;
import com.kernel360.kernelsquare.global.common_response.service.code.ServiceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum QuestionErrorCode implements ErrorCode{
    NOT_FOUND_QUESTION(HttpStatus.NOT_FOUND, QuestionServiceStatus.QUESTION_NOT_FOUND, "존재하지 않는 질문"),
    NOT_FOUND_PAGE(HttpStatus.NOT_FOUND, QuestionServiceStatus.PAGE_NOT_FOUND, "존재하지 않는 페이지");

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

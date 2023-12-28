package com.kernel360.kernelsquare.global.common_response.error.code;

import com.kernel360.kernelsquare.global.common_response.service.code.AnswerServiceStatus;
import com.kernel360.kernelsquare.global.common_response.service.code.ServiceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum AnswerErrorCode implements ErrorCode{
    ANSWER_NOT_FOUND(HttpStatus.NOT_FOUND, AnswerServiceStatus.ANSWER_NOT_FOUND, "존재하지 않는 답변");
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

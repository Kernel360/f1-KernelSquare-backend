package com.kernelsquare.core.common_response.error;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

@Slf4j
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    private static final String TRACE_ID_NAME = "request_id";
    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
        String exceptionName = throwable.getClass().getSimpleName();
        String exceptionMessage = throwable.getMessage();
        String methodName = method.getDeclaringClass().getSimpleName() + "." + method.getName();
        log.error("[{}] [Exception] {} , exceptionName = {}, exceptionMessage = {}, args = {}",
            MDC.get(TRACE_ID_NAME), methodName, exceptionName, exceptionMessage, obj);
    }
}

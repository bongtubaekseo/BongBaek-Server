package org.appjam.bongbaek.global.exception;

import org.appjam.bongbaek.global.api.ApiResponse;
import org.appjam.bongbaek.global.common.CommonErrorCode;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ApiResponse<Void> handleSampleException(CustomException e) {
        return ApiResponse.failure(e.getErrorCode());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ApiResponse<Void> handleNoHandlerFoundException(NoHandlerFoundException e) {
        return ApiResponse.failure(CommonErrorCode.INVALID_URL_ERROR);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiResponse<Void> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ApiResponse.failure(CommonErrorCode.METHOD_NOT_ALLOWED_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleInternalServerException(Exception e) {
        return ApiResponse.failure(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }
}

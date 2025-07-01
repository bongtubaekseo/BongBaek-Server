package org.appjam.bongbaek.global.exception;

import com.google.protobuf.Api;
import org.appjam.bongbaek.global.api.ApiResponse;
import org.appjam.bongbaek.global.common.CommonErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleSampleException(CustomException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ApiResponse.failure(e.getErrorCode()));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoHandlerFoundException(NoHandlerFoundException e) {
        return ResponseEntity
                .status(CommonErrorCode.INVALID_URL_ERROR.getStatus())
                .body(ApiResponse.failure(CommonErrorCode.INVALID_URL_ERROR));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity
                .status(CommonErrorCode.METHOD_NOT_ALLOWED_ERROR.getStatus())
                .body(ApiResponse.failure(CommonErrorCode.METHOD_NOT_ALLOWED_ERROR));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleInternalServerException(Exception e) {
        return ResponseEntity
                .status(CommonErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(ApiResponse.failure(CommonErrorCode.INTERNAL_SERVER_ERROR));
    }
}

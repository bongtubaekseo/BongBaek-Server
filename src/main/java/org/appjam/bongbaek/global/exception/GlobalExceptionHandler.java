package org.appjam.bongbaek.global.exception;

import org.appjam.bongbaek.global.api.ApiResponse;
import org.appjam.bongbaek.global.common.CommonErrorCode;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Objects;

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

    //NOTE: @Valid 검증 실패한 경우
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleValidationException(MethodArgumentNotValidException e) {
        return ResponseEntity
                .status(e.getStatusCode())
                .body(ApiResponse.notValid(Objects.requireNonNull(e.getFieldError()).getDefaultMessage()));
    }

    //NOTE : JSON 데이터 타입이 다른 경우
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e){
        return ResponseEntity
                .status(CommonErrorCode.BAD_REQUEST.getStatus())
                .body(ApiResponse.failure(CommonErrorCode.BAD_REQUEST));
    }

    // NOTE: ENUM 입력 데이터 오류
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        return ResponseEntity
                .status(CommonErrorCode.VALIDATION_ERROR.getStatus())
                .body(ApiResponse.failure(CommonErrorCode.VALIDATION_ERROR));
    }

    // 요청 인자들 데이터 타입이 틀린 경우
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity
                .status(CommonErrorCode.BAD_REQUEST.getStatus())
                .body(ApiResponse.failure(CommonErrorCode.BAD_REQUEST));
    }

    // 필수 입력 값이 비어있는 경우
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse<Void>> handleNullPointerException(NullPointerException e) {
        return ResponseEntity
                .status(CommonErrorCode.BAD_REQUEST.getStatus())
                .body(ApiResponse.failure(CommonErrorCode.NULL_POINTER_ERROR));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleInternalServerException(Exception e) {
        return ResponseEntity
                .status(CommonErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(ApiResponse.failure(CommonErrorCode.INTERNAL_SERVER_ERROR));
    }
}

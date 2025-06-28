package org.appjam.bongbaek.global.common;

import org.springframework.http.HttpStatus;

public enum CommonErrorCode implements ErrorCode {
    // 400 Bad Request
    BAD_REQUEST(false, HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    // 401 Unauthorized
    UNAUTHORIZED(false, HttpStatus.UNAUTHORIZED,"인증에 실패했습니다."),

    // 403 Forbidden
    FORBIDDEN(false, HttpStatus.FORBIDDEN,"권한이 없습니다."),

    // 404 Not Found
    INVALID_URL_ERROR(false, HttpStatus.NOT_FOUND, "잘못된 URL 입니다."),

    // 405 Method Not Allowed
    METHOD_NOT_ALLOWED_ERROR(false, HttpStatus.METHOD_NOT_ALLOWED, "잘못된 HTTP method 요청입니다."),

    // 500 Server Error
    INTERNAL_SERVER_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR, "서버 측 에러입니다.");

    private final boolean success;
    private final HttpStatus status;
    private final String errorMessage;

    CommonErrorCode(boolean success, HttpStatus httpStatus, String errorMessage) {
        this.success = success;
        this.status = httpStatus;
        this.errorMessage = errorMessage;
    }

    @Override
    public boolean getSuccess() {
        return this.success;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}

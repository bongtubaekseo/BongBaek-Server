package org.appjam.bongbaek.global.common;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {
    // 400 Bad Request
    BAD_REQUEST(false, HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    VALIDATION_ERROR(false, HttpStatus.BAD_REQUEST, "요청 값이 올바르지 않습니다."),
    ALREADY_REGISTERED_MEMBER(false, HttpStatus.BAD_REQUEST, "이미 가입된 사용자입니다."),
    NULL_POINTER_ERROR(false, HttpStatus.BAD_REQUEST, "필수 요청 값이 비어있습니다."),

    // 401 Unauthorized
    UNAUTHORIZED(false, HttpStatus.UNAUTHORIZED,"인증에 실패했습니다."),
    INVALID_ACCESS_TOKEN(false, HttpStatus.UNAUTHORIZED, "유효하지 않은 Access Token입니다."),
    INVALID_REFRESH_TOKEN(false, HttpStatus.UNAUTHORIZED, "유효하지 않은 Refresh Token입니다."),
    EXPIRED_ACCESS_TOKEN(false, HttpStatus.UNAUTHORIZED, "만료된 AccessToken입니다."),

    // 403 Forbidden
    FORBIDDEN(false, HttpStatus.FORBIDDEN,"권한이 없습니다."),

    // 404 Not Found
    INVALID_URL_ERROR(false, HttpStatus.NOT_FOUND, "잘못된 URL 입니다."),
    MEMBER_NOT_FOUND(false, HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    SIGN_UP_REQUIRED(false, HttpStatus.NOT_FOUND, "회원가입을 진행해주세요."),

    // 405 Method Not Allowed
    METHOD_NOT_ALLOWED_ERROR(false, HttpStatus.METHOD_NOT_ALLOWED, "잘못된 HTTP method 요청입니다."),

    // 500 Server Error
    INTERNAL_SERVER_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR, "서버 측 에러입니다.");

    private final boolean success;
    private final HttpStatus status;
    private final String errorMessage;

    @Override
    public boolean getSuccess() {
        return success;
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

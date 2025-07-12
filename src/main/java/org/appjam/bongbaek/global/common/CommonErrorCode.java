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
    UNAUTHORIZED_MALFORMED_JWT(false, HttpStatus.UNAUTHORIZED, "JWT 형식이 올바르지 않습니다."),
    UNAUTHORIZED_EXPIRATION_JWT_EXCEPTION(false, HttpStatus.UNAUTHORIZED, "JWT가 만료되었습니다."),
    UNAUTHORIZED_UNSUPPORTED_JWT(false, HttpStatus.UNAUTHORIZED, "지원하지 않는 JWT입니다."),
    INVALID_REFRESH_TOKEN(false, HttpStatus.UNAUTHORIZED, "리프레시 토큰이 유효하지 않거나 만료되었습니다."),

    // 403 Forbidden
    FORBIDDEN(false, HttpStatus.FORBIDDEN,"권한이 없습니다."),

    // 404 Not Found
    INVALID_URL_ERROR(false, HttpStatus.NOT_FOUND, "잘못된 URL 입니다."),
    MEMBER_NOT_FOUND(false, HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),

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

package org.appjam.bongbaek.global.api.code.image;

import org.appjam.bongbaek.global.api.code.ErrorResultCode;
import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode implements ErrorResultCode {
    // 400
    INVALID_IMAGE_FORMAT(HttpStatus.BAD_REQUEST, "이미지 확장자가 올바르지 않습니다"),
    INVALID_IMAGE_REQUEST(HttpStatus.BAD_REQUEST, "이미지 요청 형식이 올바르지 않습니다"),

    // 404
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "이미지 정보가 존재하지 않습니다."),

    // 500
    FAIL_READ_FILE(HttpStatus.INTERNAL_SERVER_ERROR, "파일 스트림을 읽는 중 오류가 발생했습니다.");
    private final HttpStatus status;
    private final String message;
}

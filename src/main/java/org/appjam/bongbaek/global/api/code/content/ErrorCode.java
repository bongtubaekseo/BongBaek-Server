package org.appjam.bongbaek.global.api.code.content;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.appjam.bongbaek.global.api.code.ErrorResultCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode implements ErrorResultCode {
    // 404
    CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND, "경조사 컨텐츠 정보가 존재하지 않습니다.");

    private final HttpStatus status;
    private final String message;
}

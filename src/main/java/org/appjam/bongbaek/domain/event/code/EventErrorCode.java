package org.appjam.bongbaek.domain.event.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.appjam.bongbaek.global.common.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum EventErrorCode implements ErrorCode {
    EVENT_NOT_FOUND(true, HttpStatus.NOT_FOUND,  "경조사를 찾을 수 없습니다."),
    INVALID_EVENT_DATE(false, HttpStatus.BAD_REQUEST, "유효하지 않은 경조사 날짜입니다.");

    private final boolean success;
    private final HttpStatus status;
    private final String successMessage;

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
        return successMessage;
    }
}

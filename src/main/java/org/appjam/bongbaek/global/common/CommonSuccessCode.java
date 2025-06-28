package org.appjam.bongbaek.global.common;

import org.springframework.http.HttpStatus;

public enum CommonSuccessCode implements SuccessCode {
    // 200 Ok
    OK(true, HttpStatus.OK, "요청이 성공했습니다."),

    // 201 Created
    CREATED(true, HttpStatus.CREATED, "생성되었습니다.");

    private final boolean success;
    private final HttpStatus status;
    private final String successMessage;

    CommonSuccessCode(boolean success, HttpStatus httpStatus, String successMessage) {
        this.success = success;
        this.status = httpStatus;
        this.successMessage = successMessage;
    }

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

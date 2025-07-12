package org.appjam.bongbaek.global.common;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CommonSuccessCode implements SuccessCode {
    // 200 Ok
    OK(true, HttpStatus.OK, "요청이 성공했습니다."),

    // 201 Created
    CREATED(true, HttpStatus.CREATED, "생성되었습니다."),

    // 202 Accepted
    ACCEPTED(true, HttpStatus.ACCEPTED, "회원가입을 진행해주세요."),

    // 201 Created for signup
    SIGNUP_COMPLETED(true, HttpStatus.CREATED, "회원가입이 완료되었습니다.");

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

package org.appjam.bongbaek.global.exception;

import lombok.Getter;
import org.appjam.bongbaek.global.common.ErrorCode;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

package org.appjam.bongbaek.global.common;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    boolean getSuccess();
    HttpStatus getStatus();
    String getMessage();
}

package org.appjam.bongbaek.global.common;

import org.springframework.http.HttpStatus;

public interface SuccessCode {
    boolean getSuccess();
    HttpStatus getStatus();
    String getMessage();
}

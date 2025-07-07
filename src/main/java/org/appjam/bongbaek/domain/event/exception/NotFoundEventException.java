package org.appjam.bongbaek.domain.event.exception;

import org.appjam.bongbaek.global.common.CommonErrorCode;
import org.appjam.bongbaek.global.exception.CustomException;

public class NotFoundEventException extends CustomException {
    public NotFoundEventException() {
        super(CommonErrorCode.BAD_REQUEST);
    }
}

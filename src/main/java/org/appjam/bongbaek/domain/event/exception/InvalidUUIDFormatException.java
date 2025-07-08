package org.appjam.bongbaek.domain.event.exception;

import org.appjam.bongbaek.domain.event.code.EventErrorCode;
import org.appjam.bongbaek.global.exception.CustomException;

public class InvalidUUIDFormatException extends CustomException {
    public InvalidUUIDFormatException() {
        super(EventErrorCode.INVALID_UUID_FORMAT);
    }
}

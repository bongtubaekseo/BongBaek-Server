package org.appjam.bongbaek.domain.event.exception;

import org.appjam.bongbaek.domain.event.code.EventErrorCode;
import org.appjam.bongbaek.global.exception.CustomException;

public class InvalidNoteException extends CustomException {
    public InvalidNoteException() {
        super(EventErrorCode.INVALID_NOTE_FORMAT);
    }
}

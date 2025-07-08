package org.appjam.bongbaek.domain.event.exception;

import org.appjam.bongbaek.domain.event.code.EventErrorCode;
import org.appjam.bongbaek.global.exception.CustomException;

public class NotFoundEventException extends CustomException {
  public NotFoundEventException() {
    super(EventErrorCode.EVENT_NOT_FOUND);
  }
}

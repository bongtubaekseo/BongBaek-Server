package org.appjam.bongbaek.domain.event.exception;

import org.appjam.bongbaek.domain.event.code.EventErrorCode;
import org.appjam.bongbaek.global.exception.CustomException;

public class UnauthorizationException extends CustomException {
  public UnauthorizationException() {
    super(EventErrorCode.FORBIDDEN_USER);
  }
}

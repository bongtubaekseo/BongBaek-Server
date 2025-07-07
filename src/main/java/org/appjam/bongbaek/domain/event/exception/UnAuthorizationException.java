package org.appjam.bongbaek.domain.event.exception;

import org.appjam.bongbaek.global.common.CommonErrorCode;
import org.appjam.bongbaek.global.exception.CustomException;

public class UnAuthorizationException extends CustomException {
  public UnAuthorizationException() {
    super(CommonErrorCode.UNAUTHORIZED);
  }
}

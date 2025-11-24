package org.appjam.bongbaek.global.exception.content;

import org.appjam.bongbaek.global.api.code.content.ErrorCode;
import org.appjam.bongbaek.global.exception.BaseException;


public class ContentNotFoundException extends BaseException {
  public ContentNotFoundException() {
    super(ErrorCode.CONTENT_NOT_FOUND);
  }
}

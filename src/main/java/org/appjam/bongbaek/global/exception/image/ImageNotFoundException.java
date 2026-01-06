package org.appjam.bongbaek.global.exception.image;

import org.appjam.bongbaek.global.api.code.image.ErrorCode;
import org.appjam.bongbaek.global.exception.BaseException;

public class ImageNotFoundException extends BaseException {
  public ImageNotFoundException() {
    super(ErrorCode.IMAGE_NOT_FOUND);
  }
}

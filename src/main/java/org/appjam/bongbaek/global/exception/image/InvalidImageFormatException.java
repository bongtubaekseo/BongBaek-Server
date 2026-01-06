package org.appjam.bongbaek.global.exception.image;

import org.appjam.bongbaek.global.api.code.image.ErrorCode;
import org.appjam.bongbaek.global.exception.BaseException;

public class InvalidImageFormatException extends BaseException {
    public InvalidImageFormatException() {
        super(ErrorCode.INVALID_IMAGE_FORMAT);
    }
}

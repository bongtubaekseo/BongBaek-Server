package org.appjam.bongbaek.global.exception.image;

import org.appjam.bongbaek.global.api.code.image.ErrorCode;
import org.appjam.bongbaek.global.exception.BaseException;

public class InvalidImageRequestException extends BaseException {
    public InvalidImageRequestException() {
        super(ErrorCode.INVALID_IMAGE_REQUEST);
    }
}

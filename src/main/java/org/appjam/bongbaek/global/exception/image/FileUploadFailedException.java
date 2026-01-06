package org.appjam.bongbaek.global.exception.image;

import org.appjam.bongbaek.global.api.code.image.ErrorCode;
import org.appjam.bongbaek.global.exception.BaseException;

public class FileUploadFailedException extends BaseException {
    public FileUploadFailedException() {
        super(ErrorCode.FAIL_READ_FILE);
    }
}

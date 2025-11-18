package org.appjam.bongbaek.global.exception.handler.image;

import org.appjam.bongbaek.global.api.response.ApiResponse;
import org.appjam.bongbaek.global.exception.content.ContentNotFoundException;
import org.appjam.bongbaek.global.exception.handler.BaseExceptionHandler;
import org.appjam.bongbaek.global.exception.image.FileUploadFailedException;
import org.appjam.bongbaek.global.exception.image.ImageNotFoundException;
import org.appjam.bongbaek.global.exception.image.InvalidImageFormatException;
import org.appjam.bongbaek.global.exception.image.InvalidImageRequestException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ImageExceptionHandler extends BaseExceptionHandler {
    @ExceptionHandler(FileUploadFailedException.class)
    protected ApiResponse handleContentNotFoundException(ContentNotFoundException e) {
        return buildErrorResponse(e.getErrorResultCode());
    }

    @ExceptionHandler(ImageNotFoundException.class)
    protected ApiResponse handleImageNotFoundException(ImageNotFoundException e) {
        return buildErrorResponse(e.getErrorResultCode());
    }

    @ExceptionHandler(InvalidImageRequestException.class)
    protected ApiResponse handleInvalidImageRequestException(InvalidImageRequestException e) {
        return buildErrorResponse(e.getErrorResultCode());
    }

    @ExceptionHandler(ImageNotFoundException.class)
    protected ApiResponse handleContentNotFoundException(ImageNotFoundException e) {
        return buildErrorResponse(e.getErrorResultCode());
    }

    @ExceptionHandler(InvalidImageFormatException.class)
    protected ApiResponse handleInvalidImageFormatException(InvalidImageFormatException e) {
        return buildErrorResponse(e.getErrorResultCode());
    }
}

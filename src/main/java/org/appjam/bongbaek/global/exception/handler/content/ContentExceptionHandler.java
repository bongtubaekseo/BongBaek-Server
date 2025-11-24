package org.appjam.bongbaek.global.exception.handler.content;

import org.appjam.bongbaek.global.api.response.ApiResponse;
import org.appjam.bongbaek.global.exception.content.ContentNotFoundException;
import org.appjam.bongbaek.global.exception.handler.BaseExceptionHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ContentExceptionHandler extends BaseExceptionHandler {
    @ExceptionHandler(ContentNotFoundException.class)
    protected ApiResponse handleContentNotFoundException(ContentNotFoundException e) {
        return buildErrorResponse(e.getErrorResultCode());
    }
}

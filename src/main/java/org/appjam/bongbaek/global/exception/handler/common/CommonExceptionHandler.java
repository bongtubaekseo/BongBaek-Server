package org.appjam.bongbaek.global.exception.handler.common;

import org.appjam.bongbaek.global.api.code.common.ErrorCode;
import org.appjam.bongbaek.global.api.response.ApiResponse;
import org.appjam.bongbaek.global.exception.common.RequestInvalidException;
import org.appjam.bongbaek.global.exception.handler.BaseExceptionHandler;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler extends BaseExceptionHandler {
	@ExceptionHandler(NoHandlerFoundException.class)
	protected ApiResponse handleNoHandlerFoundException(NoHandlerFoundException e) {
		log.error("존재하지 않는 엔드포인트: {}", e.getRequestURL());
		return buildErrorResponse(ErrorCode.REQUEST_PATH_INVALID);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	protected ApiResponse handleHttpRequestMethodNotSupportedException(
			HttpRequestMethodNotSupportedException e) {
		log.error("지원하지 않는 메서드: {}", e.getMethod());
		return buildErrorResponse(ErrorCode.HTTP_METHOD_INVALID);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	protected ApiResponse handleHttpMessageNotReadableException(
			HttpMessageNotReadableException e) {
		log.error("올바르지 않은 요청형태: {}", e.getMessage());
		return buildErrorResponse(ErrorCode.REQUEST_CONTENT_INVALID);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ApiResponse handleMethodArgumentTypeMismatchExceptionException(
			MethodArgumentTypeMismatchException e) {
		log.error("올바르지 않은 요청 파라미터: {}", e.getParameter());
		return buildErrorResponse(ErrorCode.REQUIRED_PARAMETER_MISSED);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	protected ApiResponse handleMissingServletRequestParameterException(
			MissingServletRequestParameterException e) {
		return buildErrorResponse(ErrorCode.REQUIRED_PARAMETER_MISSED);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ApiResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.error("올바르지 않은 요청데이터: {}", e.getBindingResult().getFieldError().getField());
		return buildErrorResponse(ErrorCode.REQUEST_CONTENT_INVALID,
				e.getBindingResult().getFieldError().getDefaultMessage());
	}

	@ExceptionHandler(RequestInvalidException.class)
	protected ApiResponse handleRequestInvalidException(RequestInvalidException e) {
		return buildErrorResponse(ErrorCode.REQUEST_CONTENT_INVALID);
	}
}

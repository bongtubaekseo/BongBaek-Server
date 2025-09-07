package org.appjam.bongbaek.global.exception.handler.member;

import org.appjam.bongbaek.global.api.response.ApiResponse;
import org.appjam.bongbaek.global.exception.handler.BaseExceptionHandler;
import org.appjam.bongbaek.global.exception.member.MemberAlreadyExistsException;
import org.appjam.bongbaek.global.exception.member.MemberAuthorityDeniedException;
import org.appjam.bongbaek.global.exception.member.MemberNotAuthenticatedException;
import org.appjam.bongbaek.global.exception.member.MemberNotFoundException;
import org.appjam.bongbaek.global.exception.member.TokenExpiredException;
import org.appjam.bongbaek.global.exception.member.TokenInvalidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MemberExceptionHandler extends BaseExceptionHandler {
	@ExceptionHandler(MemberAlreadyExistsException.class)
	protected ApiResponse handleMemberAlreadyExistsException(MemberAlreadyExistsException e) {
		return buildErrorResponse(e.getErrorResultCode());
	}

	@ExceptionHandler(MemberAuthorityDeniedException.class)
	protected ApiResponse handleMemberAuthorityDeniedException(MemberAuthorityDeniedException e) {
		return buildErrorResponse(e.getErrorResultCode());
	}

	@ExceptionHandler(MemberNotAuthenticatedException.class)
	protected ApiResponse handleMemberNotAuthenticatedException(MemberNotAuthenticatedException e) {
		return buildErrorResponse(e.getErrorResultCode());
	}

	@ExceptionHandler(MemberNotFoundException.class)
	protected ApiResponse handleMemberNotFoundException(MemberNotFoundException e) {
		return buildErrorResponse(e.getErrorResultCode());
	}

	@ExceptionHandler(TokenInvalidException.class)
	protected ApiResponse handleTokenInvalidException(TokenInvalidException e) {
		return buildErrorResponse(e.getErrorResultCode());
	}

	@ExceptionHandler(TokenExpiredException.class)
	protected ApiResponse handleTokenExpiredException(TokenExpiredException e) {
		return buildErrorResponse(e.getErrorResultCode());
	}
}

package org.appjam.bongbaek.global.jwt.exception.handler;

import org.appjam.bongbaek.global.exception.BaseException;
import org.appjam.bongbaek.global.exception.member.MemberAuthorityDeniedException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	private final HandlerExceptionResolver resolver;

	public CustomAccessDeniedHandler(
			@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver
	) {
		this.resolver = resolver;
	}

	@Override
	public void handle(
			HttpServletRequest request,
			HttpServletResponse response,
			AccessDeniedException accessDeniedException
	) {
		if (accessDeniedException.getCause() instanceof BaseException baseException) {
			resolver.resolveException(request, response, null, baseException);
			return;
		}

		resolver.resolveException(request, response, null, new MemberAuthorityDeniedException());
	}
}
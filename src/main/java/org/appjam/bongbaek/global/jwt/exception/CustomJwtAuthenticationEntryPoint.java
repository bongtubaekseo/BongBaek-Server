package org.appjam.bongbaek.global.jwt.exception;

import org.appjam.bongbaek.global.exception.BaseException;
import org.appjam.bongbaek.global.exception.member.MemberNotAuthenticatedException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomJwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final HandlerExceptionResolver resolver;

	public CustomJwtAuthenticationEntryPoint(
			@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver
	) {
		this.resolver = resolver;
	}

	@Override
	public void commence(
			HttpServletRequest request,
			HttpServletResponse response,
			AuthenticationException authException
	) {

		Object exception = request.getAttribute("exception");
		if (exception instanceof BaseException baseException) {
			resolver.resolveException(request, response, null, baseException);
		}

		resolver.resolveException(request, response, null, new MemberNotAuthenticatedException());
	}
}

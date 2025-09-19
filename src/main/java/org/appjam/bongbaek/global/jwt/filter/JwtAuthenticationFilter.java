package org.appjam.bongbaek.global.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.appjam.bongbaek.global.config.security.AuthWhiteList;
import org.appjam.bongbaek.global.exception.BaseException;
import org.appjam.bongbaek.global.exception.member.TokenExpiredException;
import org.appjam.bongbaek.global.exception.member.TokenInvalidException;
import org.appjam.bongbaek.global.jwt.JwtBlacklistManager;
import org.appjam.bongbaek.global.jwt.components.JwtParser;
import org.appjam.bongbaek.global.jwt.components.JwtValidator;
import org.appjam.bongbaek.global.jwt.data.MemberAuthentication;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private static final String ACCESS_TOKEN_PREFIX = "Bearer ";
	private static final String ACCESS_TOKEN_HEADER_KEY = "Authorization";

	private final HandlerExceptionResolver exceptionResolver;

	private final JwtValidator jwtValidator;
	private final JwtParser jwtParser;
	private final JwtBlacklistManager jwtBlacklistManager;

	public JwtAuthenticationFilter(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver,
			JwtValidator jwtValidator, JwtParser jwtParser, JwtBlacklistManager jwtBlacklistManager) {
		this.exceptionResolver = exceptionResolver;
		this.jwtValidator = jwtValidator;
		this.jwtParser = jwtParser;
		this.jwtBlacklistManager = jwtBlacklistManager;
	}

	@Override
	protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
		return AuthWhiteList.isPermitted(request.getRequestURI(), request.getMethod());
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request,
			@NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {
		final String accessTokenWithBearer = request.getHeader(ACCESS_TOKEN_HEADER_KEY);

		if (!jwtValidator.isValidFormat(accessTokenWithBearer)) {
			SecurityContextHolder.clearContext();
			resolveBaseException(request, response, new TokenInvalidException());
			return;
		}

		final String accessToken = resolveToken(accessTokenWithBearer);

		if (jwtValidator.isExpired(accessToken)) {
			SecurityContextHolder.clearContext();
			resolveBaseException(request, response, new TokenExpiredException());
			return;
		}

		if (jwtBlacklistManager.contains(accessToken)) {
			SecurityContextHolder.clearContext();
			resolveBaseException(request, response, new TokenInvalidException());
			return;
		}

		MemberAuthentication authentication = MemberAuthentication.createMemberAuthentication(
				jwtParser.getMemberId(accessToken), accessToken);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		filterChain.doFilter(request, response);
	}

	/**
	 * Header에서 Token을 추출
	 *
	 * @return Token
	 */
	private String resolveToken(String accessTokenWithBearer) {
		return accessTokenWithBearer.substring(ACCESS_TOKEN_PREFIX.length());
	}

	private void resolveBaseException(HttpServletRequest request, HttpServletResponse response, BaseException baseException){
		exceptionResolver.resolveException(request, response, null, baseException);
	}
}

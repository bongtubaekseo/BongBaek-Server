package org.appjam.bongbaek.global.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import org.appjam.bongbaek.global.config.security.util.AuthWhiteList;
import org.appjam.bongbaek.global.exception.BaseException;
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

		// 토큰이 올바른 형식인지 검증
		if (!jwtValidator.isValidFormat(accessTokenWithBearer)) {
			resolveBaseException(request, response, new TokenInvalidException());
			return;
		}

		final String accessToken = resolveToken(accessTokenWithBearer);

		// 토큰 검증
		try {
			jwtValidator.verifyToken(accessToken);
		} catch (BaseException e) {
			resolveBaseException(request, response, e);
			return;
		}

		// 블랙리스팅 여부 검증
		if (jwtBlacklistManager.contains(accessToken)) {
			resolveBaseException(request, response, new TokenInvalidException());
			return;
		}

		MemberAuthentication authentication = MemberAuthentication.createMemberAuthentication(
				jwtParser.getMemberId(accessToken));

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

	private void resolveBaseException(HttpServletRequest request, HttpServletResponse response,
			BaseException baseException) {
		SecurityContextHolder.clearContext();
		exceptionResolver.resolveException(request, response, null, baseException);
	}
}

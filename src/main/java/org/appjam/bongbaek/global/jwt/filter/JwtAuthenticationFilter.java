package org.appjam.bongbaek.global.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.appjam.bongbaek.global.config.security.AuthWhiteList;
import org.appjam.bongbaek.global.exception.member.TokenExpiredException;
import org.appjam.bongbaek.global.exception.member.TokenInvalidException;
import org.appjam.bongbaek.global.jwt.JwtBlacklistManager;
import org.appjam.bongbaek.global.jwt.components.JwtParser;
import org.appjam.bongbaek.global.jwt.components.JwtValidator;
import org.appjam.bongbaek.global.jwt.data.MemberAuthentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private static final String ACCESS_TOKEN_PREFIX = "Bearer ";
	private static final String ACCESS_TOKEN_HEADER_KEY = "Authorization";

	private final JwtValidator jwtValidator;
	private final JwtParser jwtParser;
	private final JwtBlacklistManager jwtBlacklistManager;

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
			throw new TokenInvalidException();
		}

		final String accessToken = resolveToken(accessTokenWithBearer);

		if (jwtValidator.isExpired(accessToken)) {
			SecurityContextHolder.clearContext();
			throw new TokenExpiredException();
		}

		if (jwtBlacklistManager.contains(accessToken)) {
			SecurityContextHolder.clearContext();
			throw new TokenInvalidException();
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
}

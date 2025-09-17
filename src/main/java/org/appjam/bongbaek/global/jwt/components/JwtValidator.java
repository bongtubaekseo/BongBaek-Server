package org.appjam.bongbaek.global.jwt.components;

import java.util.Date;

import javax.crypto.SecretKey;

import org.appjam.bongbaek.global.exception.member.TokenInvalidException;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtValidator {
	private static final String ACCESS_TOKEN_PREFIX = "Bearer ";

	private final SecretKey secretKey;

	/**
	 * 토큰이 Bearer로 시작하는지, null 또는 빈 문자열인지 검증
	 */
	public boolean isValidFormat(final String tokenWithBearer) {
		return tokenWithBearer != null
				&& tokenWithBearer.startsWith(ACCESS_TOKEN_PREFIX)
				&& tokenWithBearer.length() > ACCESS_TOKEN_PREFIX.length();
	}

	public boolean isExpired(final String token) {
		try{
			return Jwts.parser()
					.verifyWith(secretKey)
					.build()
					.parseSignedClaims(token)
					.getPayload()
					.getExpiration()
					.before(new Date());
		} catch (JwtException e) {
			throw new TokenInvalidException();
		}
	}
}

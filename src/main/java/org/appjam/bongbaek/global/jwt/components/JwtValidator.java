package org.appjam.bongbaek.global.jwt.components;

import java.util.Date;

import org.appjam.bongbaek.global.exception.member.SignatureInvalidException;
import org.appjam.bongbaek.global.exception.member.TokenExpiredException;
import org.appjam.bongbaek.global.exception.member.TokenInvalidException;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtValidator {
	private static final String ACCESS_TOKEN_PREFIX = "Bearer ";

	private final String issuer;

	private final JwtParser jwtParser;

	/**
	 * 토큰이 Bearer로 시작하는지, null 또는 빈 문자열인지 검증
	 */
	public boolean isValidFormat(final String tokenWithBearer) {
		return tokenWithBearer != null
				&& tokenWithBearer.startsWith(ACCESS_TOKEN_PREFIX)
				&& tokenWithBearer.length() > ACCESS_TOKEN_PREFIX.length();
	}

	public void verifyToken(final String token) {
		Jws<Claims> claims = parseAndVerifySignature(token);
		verifyClaims(claims);
	}

	private Jws<Claims> parseAndVerifySignature(final String token) {
		try {
			return jwtParser.parseClaims(token);
		} catch (JwtException e) {
			throw new SignatureInvalidException();
		}
	}

	private void verifyClaims(final Jws<Claims> claims){
		if(!isValidIssuer(claims)) {
			throw new TokenInvalidException();
		}

		if(isExpired(claims)) {
			throw new TokenExpiredException();
		}
	}

	private boolean isValidIssuer(final Jws<Claims> claims) {
		return claims.getPayload()
				.getIssuer()
				.equals(issuer);
	}

	private boolean isExpired(final Jws<Claims> claims) {
		return claims.getPayload()
				.getExpiration()
				.before(new Date());
	}
}

package org.appjam.bongbaek.global.jwt.components;

import javax.crypto.SecretKey;

import org.appjam.bongbaek.global.exception.member.TokenInvalidException;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtValidator {

	private final SecretKey secretKey;

	/**
	 * 토큰 유효성 검증
	 * - 성공하면 true 반환
	 * - 실패하면 CustomException 바로 던짐
	 */
	public boolean validateToken(String token) {
		try {
			Jwts.parser()
					.verifyWith(secretKey)
					.build()
					.parseSignedClaims(token);

			return true;
		} catch (SecurityException e) {
			throw new TokenInvalidException();
		} catch (JwtException e) {
			throw new TokenInvalidException();
		}
	}

	/**
	 * 토큰이 Bearer로 시작하는지 확인
	 */
	public boolean isBearer(String token) {
		return token != null && token.startsWith("Bearer ");
	}
}
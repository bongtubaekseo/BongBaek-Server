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
public class JwtParser {

	// 나중에 role 도입시 사용
	private static final String CLAIM_ROLE = "role";

	private final SecretKey secretKey;

	/**
	 * Access Token을 파싱하여 멤버의 id를 반환하는 메서드
	 */
	public String getMemberId(String token) {
		try{
			return Jwts.parser()
					.verifyWith(secretKey)
					.build()
					.parseSignedClaims(token)
					.getPayload()
					.getSubject();
		} catch (JwtException e) {
			throw new TokenInvalidException();
		}
	}

	public Date getExpire(final String token) {
		try {
			return Jwts.parser()
					.verifyWith(secretKey)
					.build()
					.parseSignedClaims(token)
					.getPayload()
					.getExpiration();
		} catch (JwtException e) {
			throw new TokenInvalidException();
		}
	}
}
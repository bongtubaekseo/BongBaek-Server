package org.appjam.bongbaek.global.jwt.components;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtParser {

	// 나중에 role 도입시 사용
	private static final String CLAIM_ROLE = "role";

	private final SecretKey secretKey;

	public String getMemberId(final String token) {
		return parseClaims(token).getPayload()
				.getSubject();
	}

	public Date getExpire(final String token) {
		return parseClaims(token).getPayload()
				.getExpiration();
	}

	public Jws<Claims> parseClaims(final String token) {
		return Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token);
	}
}
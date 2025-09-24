package org.appjam.bongbaek.global.jwt.components;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;

import javax.crypto.SecretKey;

import lombok.RequiredArgsConstructor;

import org.appjam.bongbaek.domain.member.entity.Member;
import org.appjam.bongbaek.global.jwt.dto.TokenInfo;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

	private final SecretKey secretKey;
	private final String issuer;
	private final long accessTokenExpireIn;
	private final long refreshTokenExpireIn;

	/**
	 * Access Token 생성
	 */
	public TokenInfo generateAccessToken(final Member member) {
		return generateToken(member, accessTokenExpireIn);
	}

	/**
	 * Refresh Token 생성
	 */
	public TokenInfo generateRefreshToken(final Member member) {
		return generateToken(member, refreshTokenExpireIn);
	}

	private TokenInfo generateToken(final Member member, final long expiration) {
		long expiredAt = System.currentTimeMillis() + expiration;

		String token = Jwts.builder()
				.issuer(issuer)
				.subject(member.getMemberId())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(expiredAt))
				.signWith(secretKey, SIG.HS512)
				.compact();

		return TokenInfo.of(token, expiredAt);
	}
}
package org.appjam.bongbaek.global.oauth;

import java.security.interfaces.RSAPublicKey;
import java.time.Instant;

import org.appjam.bongbaek.global.exception.member.SignatureInvalidException;
import org.appjam.bongbaek.global.exception.member.TokenExpiredException;
import org.appjam.bongbaek.global.exception.member.TokenInvalidException;
import org.appjam.bongbaek.global.oauth.resources.OAuthProperty;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

public class OidcValidator {
	public static void validateSignature(SignedJWT signedJWT, RSAPublicKey publicKey) {
		// 토큰 서명 검증
		if (!verifySignature(signedJWT, publicKey)) {
			throw new SignatureInvalidException();
		}
	}

	public static void validateClaims(JWTClaimsSet claims, OAuthProperty property) {
		// 토큰 발행 기관 검증
		validateIssuer(claims, property.issuer());
		// 토큰 발행 요청자 검증
		validateAudience(claims, property.clientId());
		// 토큰 만료 여부 검증
		validateExpiration(claims);
	}

	private static boolean verifySignature(SignedJWT signedJWT, RSAPublicKey publicKey) {
		try {
			return signedJWT.verify(new RSASSAVerifier(publicKey));
		} catch (JOSEException e) {
			throw new SignatureInvalidException();
		}
	}

	private static void validateIssuer(JWTClaimsSet claims, String expectedIssuer) {
		if (!expectedIssuer.equals(claims.getIssuer())) {
			throw new TokenInvalidException();
		}
	}

	private static void validateAudience(JWTClaimsSet claims, String expectedAudience) {
		if (!claims.getAudience().contains(expectedAudience)) {
			throw new TokenInvalidException();
		}
	}

	private static void validateExpiration(JWTClaimsSet claims) {
		if (claims.getExpirationTime().toInstant().isBefore(Instant.now())) {
			throw new TokenExpiredException();
		}
	}
}

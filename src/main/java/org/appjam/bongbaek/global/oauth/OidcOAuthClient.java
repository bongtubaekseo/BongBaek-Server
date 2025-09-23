package org.appjam.bongbaek.global.oauth;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;

import org.appjam.bongbaek.global.exception.member.OAuthProviderInvalidException;
import org.appjam.bongbaek.global.oauth.jwk.JwkProvider;
import org.appjam.bongbaek.global.oauth.jwk.RSAPublicKeyConverter;
import org.appjam.bongbaek.global.oauth.resources.OAuthProperties;
import org.appjam.bongbaek.global.oauth.resources.OAuthProperty;
import org.appjam.bongbaek.global.oauth.util.OAuthIdTokenParser;
import org.appjam.bongbaek.global.oauth.util.OAuthIdTokenValidator;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OidcOAuthClient {
	private final OAuthProperties properties;
	private final JwkProvider jwkProvider;

	public String getUserInfo(String provider, String oauthIdToken) {
		OAuthProperty property = getOAuthProperty(provider);

		// id token을 문자열에서 객체로 반환
		SignedJWT signedJWT = OAuthIdTokenParser.parseJWT(oauthIdToken);

		// id token에서 kid 추출
		String kid = OAuthIdTokenParser.getKeyID(signedJWT);

		// id toekn의 kid와 일치하는 jwk 가져오기
		JWK jwk = jwkProvider.getJWK(provider, getOauthUrl(property), kid);

		JWTClaimsSet claims = verifyAndExtractClaims(property, signedJWT, jwk);

		return claims.getSubject();
	}

	private JWTClaimsSet verifyAndExtractClaims(OAuthProperty property, SignedJWT signedJWT, JWK jwk) {
		JWTClaimsSet claims = OAuthIdTokenParser.getJWTClaimsSet(signedJWT);	// id token의 claim 추출
		RSAPublicKey publicKey = RSAPublicKeyConverter.convert(jwk);	// jwk의 n, e로 공개키 객체 생성

		OAuthIdTokenValidator.verifySignature(signedJWT, publicKey);	// 공개키로 id token의 서명 검증
		OAuthIdTokenValidator.validateClaims(claims, property);		// claim 검증

		return claims;
	}

	private OAuthProperty getOAuthProperty(String provider) {
		return properties.getOAuthProperty(provider)
				.orElseThrow(OAuthProviderInvalidException::new);
	}

	private URL getOauthUrl(OAuthProperty oAuthProperty) {
		try {
			return URI.create(oAuthProperty.publicKeyUri()).toURL();
		} catch (MalformedURLException e) {
			throw new OAuthProviderInvalidException();
		}
	}
}

package org.appjam.bongbaek.global.oauth;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;

import org.appjam.bongbaek.global.exception.member.OAuthProviderInvalidException;
import org.appjam.bongbaek.global.exception.member.TokenInvalidException;
import org.appjam.bongbaek.global.oauth.resources.OAuthProperties;
import org.appjam.bongbaek.global.oauth.resources.OAuthProperty;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OidcOAuthClient {
	private final OAuthProperties properties;

	public String getUserInfo(String provider, String oauthIdToken) {
		OAuthProperty property = this.getOAuthProperty(provider);

		// id token파싱해서 kid 가져오기
		SignedJWT signedJWT = this.parseJWT(oauthIdToken);
		String kid = signedJWT.getHeader().getKeyID();

		// 키 목록에서 kid와 일치하는 키 찾기
		JWK jwk = getJWKSet(property).getKeyByKeyId(kid);

		// id token의 claim 추출
		JWTClaimsSet claims = this.getClaims(signedJWT);

		// 공개키의 n, e로 키 객체 생성 후 검증
		OidcValidator.validateSignature(signedJWT, this.createRSAPublicKey(jwk));
		// claim 검증
		OidcValidator.validateClaims(claims, property);

		System.out.println(claims.getSubject());
		return claims.getSubject();
	}

	private OAuthProperty getOAuthProperty(String provider) {
		return properties.getOAuthProperty(provider)
				.orElseThrow(() -> new RuntimeException("OAuth property not found"));
	}

	private SignedJWT parseJWT(String oauthIdToken) {
		try {
			return SignedJWT.parse(oauthIdToken);
		} catch (ParseException e) {
			throw new TokenInvalidException();
		}
	}

	private JWKSet getJWKSet(OAuthProperty oAuthProperty){
		try{
			return JWKSet.load(getOauthUrl(oAuthProperty));
		} catch (ParseException | IOException e) {
			throw new OAuthProviderInvalidException();
		}
	}

	private URL getOauthUrl(OAuthProperty oAuthProperty) {
		try{
			return URI.create(oAuthProperty.publicKeyUri()).toURL();
		} catch (MalformedURLException e) {
			throw new OAuthProviderInvalidException();
		}
	}

	private RSAPublicKey createRSAPublicKey(JWK jwk){
		try{
			return ((RSAKey) jwk).toRSAPublicKey();
		} catch (JOSEException e) {
			throw new OAuthProviderInvalidException();
		}
	}

	private JWTClaimsSet getClaims(SignedJWT signedJWT){
		try{
			return signedJWT.getJWTClaimsSet();
		} catch (ParseException e) {
			throw new TokenInvalidException();
		}
	}
}

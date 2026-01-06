package org.appjam.bongbaek.global.oauth.jwk;

import java.security.interfaces.RSAPublicKey;

import org.appjam.bongbaek.global.exception.member.OAuthProviderInvalidException;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class RSAPublicKeyConverter {
	public static RSAPublicKey convert(JWK jwk) {
		try {
			return ((RSAKey)jwk).toRSAPublicKey();
		} catch (JOSEException e) {
			throw new OAuthProviderInvalidException();
		}
	}
}

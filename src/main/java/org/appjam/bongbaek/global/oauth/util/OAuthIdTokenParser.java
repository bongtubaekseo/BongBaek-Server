package org.appjam.bongbaek.global.oauth.util;

import java.text.ParseException;

import org.appjam.bongbaek.global.exception.member.TokenInvalidException;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OAuthIdTokenParser {
	public static SignedJWT parseJWT(String oauthIdToken) {
		try {
			return SignedJWT.parse(oauthIdToken);
		} catch (ParseException e) {
			throw new TokenInvalidException();
		}
	}

	public static String getKeyID(SignedJWT signedJWT){
		return signedJWT.getHeader().getKeyID();
	}

	public static JWTClaimsSet getJWTClaimsSet(SignedJWT signedJWT){
		try{
			return signedJWT.getJWTClaimsSet();
		} catch (ParseException e) {
			throw new TokenInvalidException();
		}
	}
}

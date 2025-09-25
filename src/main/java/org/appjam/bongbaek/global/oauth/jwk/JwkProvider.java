package org.appjam.bongbaek.global.oauth.jwk;

import java.net.URL;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

import org.appjam.bongbaek.global.exception.member.OAuthProviderInvalidException;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKMatcher;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.JWKSourceBuilder;
import com.nimbusds.jose.proc.SecurityContext;

@Component
public class JwkProvider {
	private static final int CACHE_TTL = 5 * 60 * 1000;
	private static final int CACHE_REFRESH_TIMEOUT = 60 * 1000;

	private final Map<String, JWKSource<SecurityContext>> jwkSourceCache = new ConcurrentHashMap<>();

	public JWK getJWK(String provider, URL url, String kid){
		JWKSelector jwkSelector = new JWKSelector(new JWKMatcher.Builder().keyID(kid).build());

		try {
			return getJwkSource(provider, url).get(jwkSelector, null).getFirst();
		} catch (KeySourceException | NoSuchElementException e) {
			throw new OAuthProviderInvalidException();
		}
	}

	public JWKSource<SecurityContext> getJwkSource(String provider, URL url) {
		return jwkSourceCache.computeIfAbsent(provider, p -> createJwkSource(url));

	}

	private JWKSource<SecurityContext> createJwkSource(URL url) {
		return JWKSourceBuilder
				.create(url)
				.cache(CACHE_TTL, CACHE_REFRESH_TIMEOUT)
				.retrying(true)
				.build();
	}
}

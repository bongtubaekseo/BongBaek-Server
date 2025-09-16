package org.appjam.bongbaek.global.oauth.resources;

public record OAuthProperty(
		String clientId,
		String issuer,
		String publicKeyUri
) {
}

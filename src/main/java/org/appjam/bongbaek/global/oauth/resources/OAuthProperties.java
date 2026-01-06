package org.appjam.bongbaek.global.oauth.resources;

import java.util.Map;
import java.util.Optional;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ConfigurationProperties(prefix = "oauth")
public class OAuthProperties {
	private final Map<String, OAuthProperty> properties;

	public Optional<OAuthProperty> getOAuthProperty(String provider) {
		return Optional.ofNullable(properties.get(provider));
	}
}

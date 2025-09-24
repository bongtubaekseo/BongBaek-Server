package org.appjam.bongbaek.global.config.security;

import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.appjam.bongbaek.global.config.security.util.JwtProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {
	private final JwtProperties jwtProperties;

	@Bean
	public SecretKey secretKey() {
		return Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));
	}

	@Bean
	public String issuer() {
		return jwtProperties.issuer();
	}

	@Bean
	public long accessTokenExpireIn() {
		return jwtProperties.accessTokenExpireIn();
	}

	@Bean
	public long refreshTokenExpireIn() {
		return jwtProperties.refreshTokenExpireIn();
	}
}
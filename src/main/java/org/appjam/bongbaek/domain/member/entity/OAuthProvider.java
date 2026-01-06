package org.appjam.bongbaek.domain.member.entity;

import java.util.Arrays;

import org.appjam.bongbaek.global.exception.member.OAuthProviderInvalidException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuthProvider {
	KAKAO("kakao"),
	APPLE("apple"),
	GOOGLE("google");

	private final String name;

	public static OAuthProvider of(String name) {
		return Arrays.stream(OAuthProvider.values())
				.filter(oAuthProvider -> oAuthProvider.name.equals(name))
				.findFirst()
				.orElseThrow(OAuthProviderInvalidException::new);
	}
}

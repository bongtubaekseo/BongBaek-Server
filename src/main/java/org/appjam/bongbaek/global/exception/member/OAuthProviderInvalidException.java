package org.appjam.bongbaek.global.exception.member;

import org.appjam.bongbaek.global.api.code.member.ErrorCode;
import org.appjam.bongbaek.global.exception.BaseException;

public class OAuthProviderInvalidException extends BaseException {
	public OAuthProviderInvalidException() {
		super(ErrorCode.OAUTH_PROVIDER_INVALID);
	}
}

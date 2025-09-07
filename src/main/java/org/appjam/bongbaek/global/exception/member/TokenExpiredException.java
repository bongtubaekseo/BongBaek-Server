package org.appjam.bongbaek.global.exception.member;

import org.appjam.bongbaek.global.api.code.member.ErrorCode;
import org.appjam.bongbaek.global.exception.BaseException;

public class TokenExpiredException extends BaseException {
	public TokenExpiredException() {
		super(ErrorCode.TOKEN_EXPIRED);
	}
}

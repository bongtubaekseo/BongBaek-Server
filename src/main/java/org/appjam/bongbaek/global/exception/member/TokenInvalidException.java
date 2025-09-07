package org.appjam.bongbaek.global.exception.member;

import org.appjam.bongbaek.global.api.code.member.ErrorCode;
import org.appjam.bongbaek.global.exception.BaseException;

public class TokenInvalidException extends BaseException {
	public TokenInvalidException() {
		super(ErrorCode.TOKEN_INVALID);
	}
}

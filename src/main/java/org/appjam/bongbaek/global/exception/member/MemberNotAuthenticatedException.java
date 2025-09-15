package org.appjam.bongbaek.global.exception.member;

import org.appjam.bongbaek.global.api.code.member.ErrorCode;
import org.appjam.bongbaek.global.exception.BaseException;

public class MemberNotAuthenticatedException extends BaseException {
	public MemberNotAuthenticatedException() {
		super(ErrorCode.AUTHENTICATE_FAIL);
	}
}

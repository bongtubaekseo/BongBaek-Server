package org.appjam.bongbaek.global.exception.member;

import org.appjam.bongbaek.global.api.code.member.ErrorCode;
import org.appjam.bongbaek.global.exception.BaseException;

public class SignatureInvalidException extends BaseException {
	public SignatureInvalidException() {
		super(ErrorCode.SIGNATURE_INVALID);
	}
}

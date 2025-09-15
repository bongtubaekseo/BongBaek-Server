package org.appjam.bongbaek.global.exception.member;

import org.appjam.bongbaek.global.api.code.member.ErrorCode;
import org.appjam.bongbaek.global.exception.BaseException;

public class MemberAlreadyExistsException extends BaseException {
	public MemberAlreadyExistsException() {
		super(ErrorCode.MEMBER_ALREADY_EXISTS);
	}
}

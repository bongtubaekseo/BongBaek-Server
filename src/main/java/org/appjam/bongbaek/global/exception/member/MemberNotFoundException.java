package org.appjam.bongbaek.global.exception.member;

import org.appjam.bongbaek.global.api.code.member.ErrorCode;
import org.appjam.bongbaek.global.exception.BaseException;

public class MemberNotFoundException extends BaseException {
	public MemberNotFoundException() {
		super(ErrorCode.MEMBER_NOT_FOUND);
	}
}

package org.appjam.bongbaek.global.exception.member;

import org.appjam.bongbaek.global.api.code.member.ErrorCode;
import org.appjam.bongbaek.global.exception.BaseException;

public class MemberAuthorityDeniedException extends BaseException {
	public MemberAuthorityDeniedException() {
		super(ErrorCode.AUTHORITY_DENIED);
	}
}

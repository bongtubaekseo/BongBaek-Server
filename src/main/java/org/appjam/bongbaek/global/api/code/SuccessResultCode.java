package org.appjam.bongbaek.global.api.code;

public interface SuccessResultCode extends ResultCode {
	default boolean isSuccess() {
		return true;
	}
}

package org.appjam.bongbaek.global.api.code;

public interface ErrorResultCode extends ResultCode {
	default boolean isSuccess() {
		return false;
	}
}

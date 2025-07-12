package org.appjam.bongbaek.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.appjam.bongbaek.global.common.CommonErrorCode;
import org.appjam.bongbaek.global.exception.CustomException;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum IncomeType {
	OVER200("200만원 이상"),
	UNDER200("200만원 이하"),
	NONE("없음");

	private final String description;

	public static IncomeType of(String description) {
		return Arrays.stream(IncomeType.values())
				.filter(incomeType -> incomeType.description.equals(description))
				.findFirst()
				.orElseThrow(() -> new CustomException(CommonErrorCode.BAD_REQUEST));
	}
}

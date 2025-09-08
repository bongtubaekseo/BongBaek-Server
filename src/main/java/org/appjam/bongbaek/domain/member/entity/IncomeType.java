package org.appjam.bongbaek.domain.member.entity;

import java.util.Arrays;

import org.appjam.bongbaek.global.exception.common.RequestInvalidException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IncomeType {
	OVER200("200만원 이상"),
	UNDER200("200만원 미만"),
	NONE("없음");

	private final String description;

	public static IncomeType of(String description) {
		return Arrays.stream(IncomeType.values())
				.filter(incomeType -> incomeType.description.equals(description))
				.findFirst()
				.orElseThrow(RequestInvalidException::new);
	}
}

package org.appjam.bongbaek.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IncomeType {
	OVER200("200만원 이상"),
	UNDER200("200만원 이하");

	private final String description;
}

package org.appjam.bongbaek.domain.event.entity;

import java.util.Arrays;
import org.appjam.bongbaek.global.exception.common.RequestInvalidException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
	WEDDING("결혼식", 80_000),
	FUNERAL("장례식", 50_000),
	DOLJANCHI("돌잔치", 50_000),
	BIRTHDAY("생일", 30_000);

	private final String description;
	private final int defaultCost;

	public static Category of(String description) {
		if (description == null)
			return null;
		return Arrays.stream(Category.values())
				.filter(category -> category.description.equals(description))
				.findFirst()
				.orElseThrow(RequestInvalidException::new);
	}
}

package org.appjam.bongbaek.domain.event.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
	WEDDING("결혼식"),
	FUNERAL("장례식"),
	DOLJANCHI("돌잔치"),
	BIRTHDAY("생일");

	private final String description;
}

package org.appjam.bongbaek.domain.event.entity;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Relationship {
	FAMILY("가족/친척", 1.7f),
	FRIEND("친구", 1.7f),
	COWORKER("직장", 1.3f),
	ALUMNI("선후배", 1.3f),
	NEIGHBOR("이웃", 1.2f),
	ETC("기타", 1.3f);

	private final String description;
	private final float score;

	public static Relationship of(String description) {
		return Arrays.stream(Relationship.values())
				.filter(relationship -> relationship.description.equals(description))
				.findFirst()
				.orElse(null);
	}
}

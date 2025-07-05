package org.appjam.bongbaek.domain.event.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Relationship {
	FAMILY("가족/친척"),
	FRIEND("친구"),
	COWORKER("직장"),
	ALUMNI("선후배"),
	NEIGHBOR("이웃"),
	ETC("기타");

	private final String description;
}

package org.appjam.bongbaek.domain.event.test.dto.common;

import java.time.LocalDate;

import org.appjam.bongbaek.domain.event.entity.Event;

public record EventInfo(
		String eventCategory,
		String relationship,
		int cost,
		boolean isAttend,
		LocalDate eventDate
) {
	public static EventInfo from(Event event) {
		return new EventInfo(event.getEventCategory().getDescription(),
				event.getRelationship().getDescription(),
				event.getCost(),
				event.isAttended(),
				event.getEventDate());
	}
}

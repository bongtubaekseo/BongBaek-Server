package org.appjam.bongbaek.domain.event.dto.common;

import java.time.LocalDate;

import org.appjam.bongbaek.domain.event.entity.Event;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public record EventInfo(
		String eventCategory,
		String relationship,
		int cost,
		Boolean isAttend,
		LocalDate eventDate,
		Integer dDay,
		String note
) {
	public static EventInfo from(Event event) {
		return new EventInfo(event.getEventCategory().getDescription(),
				event.getRelationship().getDescription(),
				event.getCost(),
				event.isAttended(),
				event.getEventDate(),
				null,
				event.getNote());
	}
	public static EventInfo from(
			String eventCategory,
			String relationship,
			int cost,
			LocalDate eventDate,
			Integer dDay
	) {
		return new EventInfo(
				eventCategory,
				relationship,
				cost,
				null,
				eventDate,
				dDay,
				null);
	}
}

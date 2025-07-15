package org.appjam.bongbaek.domain.event.dto.common;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Max;
import org.appjam.bongbaek.domain.event.entity.Event;

public record EventInfo(
		String eventCategory,
		String relationship,
		@Max(value = 99999999, message = "경조사비는 99,999,999원 이하여야 합니다")
		int cost,
		@JsonInclude(JsonInclude.Include.NON_NULL)
		Boolean isAttend,
		LocalDate eventDate,
		@JsonInclude(JsonInclude.Include.NON_NULL)
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

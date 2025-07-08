package org.appjam.bongbaek.domain.event.dto.common;

import java.time.LocalDate;

import jakarta.validation.constraints.Size;
import org.appjam.bongbaek.domain.event.entity.Event;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public record EventInfo(
		String eventCategory,
		String relationship,
		@Size(max = 99999999, message = "2자 이상 10자 이내만 기입할 수 있습니다")
		int cost,
		Boolean isAttend,
		LocalDate eventDate,
		Integer dDay,
		@Size(max = 50, message= "메모는 50자를 넘길 수 없습니다")
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

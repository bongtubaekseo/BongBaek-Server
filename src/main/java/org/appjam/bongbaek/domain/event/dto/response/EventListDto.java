package org.appjam.bongbaek.domain.event.dto.response;

import java.util.List;
import java.util.UUID;

import org.appjam.bongbaek.domain.event.entity.Event;
import org.appjam.bongbaek.domain.event.dto.common.EventInfo;
import org.appjam.bongbaek.domain.event.dto.common.HostInfo;
import org.springframework.data.domain.Slice;

public record EventListDto(
		List<EventListElement> events,
		int currentPage,
		boolean isLast
) {
	public static EventListDto of(Slice<Event> events){
		List<EventListElement> elements = events.getContent().stream()
				.map(EventListElement::from)
				.toList();

		return new EventListDto(elements, events.getNumber(), events.isLast());
	}

	private record EventListElement(UUID eventId, HostInfo hostInfo, EventInfo eventInfo) {
		private static EventListElement from(Event event) {
			return new EventListElement(event.getEventId(), HostInfo.from(event),
					EventInfo.from(
							event.getEventCategory().getDescription(),
							event.getRelationship().getDescription(),
							event.getCost(),
							event.getEventDate(),null));
		}
	}
}

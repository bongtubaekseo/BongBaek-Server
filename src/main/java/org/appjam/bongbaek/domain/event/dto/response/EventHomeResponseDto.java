package org.appjam.bongbaek.domain.event.dto.response;

import org.appjam.bongbaek.domain.event.dto.common.EventInfo;
import org.appjam.bongbaek.domain.event.dto.common.HostInfo;
import org.appjam.bongbaek.domain.event.dto.common.LocationInfo;
import org.appjam.bongbaek.domain.event.entity.Event;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;

public record EventHomeResponseDto (
        List<EventResponseDto> events
){

    public static EventHomeResponseDto from(List<Event> events) {
          return new EventHomeResponseDto(
                         events.stream()
                              .map(EventResponseDto::from)
                              .toList()
                     );
    }

    private record EventResponseDto (UUID eventId, HostInfo hostInfo, EventInfo eventInfo, LocationInfo locationInfo) {

        private static EventResponseDto from(Event event) {
            return new  EventResponseDto(event.getEventId(),
                    HostInfo.from(event),
                    EventInfo.from(
                            event.getEventCategory().getDescription(),
                            event.getRelationship().getDescription(),
                            event.getCost(),
                            event.getEventDate(),
                            Period.between(LocalDate.now(), event.getEventDate()).getDays()
                    ),
            LocationInfo.from(event.getLocation()));
        }
    }
}



package org.appjam.bongbaek.domain.event.dto.response;

import org.appjam.bongbaek.domain.event.dto.common.*;
import org.appjam.bongbaek.domain.event.entity.Event;

import java.util.UUID;

public record EventDetailResponseDto(
        UUID eventId,
        HostInfo hostInfo,
        EventInfo eventInfo,
        LocationInfo locationInfo
) {
    public static EventDetailResponseDto of(Event event) {
        return new EventDetailResponseDto(
                event.getEventId(),
                HostInfo.from(event),
                EventInfo.from(event),
                LocationInfo.from(event)
        );
    }
}

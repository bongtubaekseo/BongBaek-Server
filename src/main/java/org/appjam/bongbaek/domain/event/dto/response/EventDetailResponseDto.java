package org.appjam.bongbaek.domain.event.dto.response;

import org.appjam.bongbaek.domain.event.dto.common.*;
import org.appjam.bongbaek.domain.event.entity.Event;

public record EventDetailResponseDto(
        String eventId,
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